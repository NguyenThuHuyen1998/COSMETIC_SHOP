package com.example.crud.controller;

import com.example.crud.constants.InputParam;
import com.example.crud.entity.Category;
import com.example.crud.entity.Product;
import com.example.crud.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ProductController {
    public static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;
    private final CategoryService categoryService;
    private final JwtService jwtService;
    private final OrderService orderService;
    private final FilesStorageService filesStorageService;
//    private FileService fileService;

    @Value("${file.upload-dir}")
    private String fileDir;

    @Value(("${server.port}"))
    private int port;

//    @Autowired
//    private Drive googleDrive;

    public static final String userDir= "file:///"+ System.getProperty("user.dir");
//file:///home/huyenbaby/Downloads/CRUDProduct/uploads/Screenshot%20from%202020-12-29%2000-26-46.png
    @Autowired
    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             JwtService jwtService,
                             OrderService orderService,
                             FilesStorageService fileStorageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.jwtService= jwtService;
        this.orderService= orderService;
        this.filesStorageService= fileStorageService;
    }

    @Autowired
    public FilesStorageService fileStorageService;

    @GetMapping("/test")
    public ResponseEntity<String> testUploadFile(@RequestParam("file") MultipartFile file){
        return null;
    }
    //lấy danh sách tất cả sản phẩm dành cho user
    // lọc theo category, khoảng giá, search keyword
    @CrossOrigin
    @ResponseBody
    @GetMapping(value = "/products")
    public ResponseEntity<Product> getAllProduct(@RequestParam(required = false, defaultValue = "") String keyword,
                                         @RequestParam(required = false, defaultValue = "-1") double priceMin,
                                         @RequestParam(required = false, defaultValue = "-1") double priceMax,
                                         @RequestParam(required = false, defaultValue = "0") long categoryId,
                                         @RequestParam(required = false, defaultValue = "") String sortBy) throws ParseException {
            Map<String, Object> input = new HashMap<>();
            input.put(InputParam.KEY_WORD, keyword);
            input.put(InputParam.PRICE_MAX, priceMax);
            input.put(InputParam.PRICE_MIN, priceMin);
            input.put(InputParam.CATEGORY_ID, categoryId);
            input.put(InputParam.SORT_BY, sortBy);
            List<Product> output = productService.filterProduct(input);
            return new ResponseEntity(output, HttpStatus.OK);
    }

    @GetMapping(value = "/products/bestSeller")
    public ResponseEntity<Product> getListProductBestSeller(@RequestParam(required = false, defaultValue = "5") int limit) throws ParseException {
        Map<Long, Integer> result= orderService.getListProductBestSeller();
        Set<Map.Entry<Long, Integer>> entries= result.entrySet();
        Comparator<Map.Entry<Long, Integer>> comparator = new Comparator<Map.Entry<Long, Integer>>() {
            @Override
            public int compare(Map.Entry<Long, Integer> e1, Map.Entry<Long, Integer> e2) {
                Integer v1 = e1.getValue();
                Integer v2 = e2.getValue();
                return v2.compareTo(v1);
            }
        };
        List<Map.Entry<Long, Integer>> listEntries = new ArrayList(entries);
        Collections.sort(listEntries, comparator);
        LinkedHashMap<Long, Integer> sortedMap = new LinkedHashMap<>(listEntries.size());
        for (Map.Entry<Long, Integer> entry : listEntries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        Set<Map.Entry<Long, Integer>> sortedEntries = sortedMap.entrySet();
        List<Long> listProduct= new ArrayList<>();
        for (Map.Entry<Long, Integer> mapping : sortedEntries) {
            listProduct.add(mapping.getKey());
        }
        List<Product> productList= new ArrayList<>();
        for (int i=0; i<limit; i++){
            productList.add(productService.findById(listProduct.get(i)));
        }
        return new ResponseEntity(productList, HttpStatus.OK);
    }

    // xem chi tiết 1 sản phẩm
    @CrossOrigin
    @GetMapping(value = "products/{id}")
    public ResponseEntity<Product> getAProduct(@PathVariable("id") long productId) {
        try{
            Product currentProduct = productService.findById(productId);
            if(!currentProduct.isActive()) {
                return new ResponseEntity("Sản phẩm đã ngừng bán, vui lòng chọn sản phẩm khác!", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity(currentProduct, HttpStatus.OK);
        }
        catch (Exception e){
            logger.error(String.valueOf(e));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    //---------------------------------------ADMIN--------------------------------------------------


    @CrossOrigin
    @PostMapping(value = "/adminPage/products")
    public ResponseEntity<Product> addProduct(@RequestParam("name") String name,
                                              @RequestParam("price") double price,
                                              @RequestParam("categoryId") long categoryId,
                                              @RequestParam("description") String description,
                                              @RequestParam("preview") String preview,
                                              @RequestParam("image") MultipartFile file,
                                              HttpServletRequest request) {
        if(jwtService.isAdmin(request)) {
            Product product= new Product();
            try {
                Category category = categoryService.findById(categoryId);
                if (category== null){
                    return new ResponseEntity("Danh mục không tồn tại", HttpStatus.BAD_REQUEST);
                }
                product.setCategory(category);
                product.setName(name);
                product.setDateAdd(new Date().getTime());
                product.setDescription(description);
                product.setPreview(preview);
                product.setPrice(price);
                product.setActive(true);
                String fileName= filesStorageService.save(file);
                System.out.println(fileDir);
                Path path = Paths.get(fileDir);
                Path resolvedPath
                        = path.resolve(fileName);
                System.out.println(resolvedPath.toString());
                product.setImage("http://localhost:"+ String.valueOf(port)+resolvedPath);
                productService.save(product);
            } catch (Exception e) {
                logger.error(String.valueOf(e));
                return new ResponseEntity("Kiểm tra thông tin đầu vào", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.METHOD_NOT_ALLOWED);
    }

    // cập nhật tt 1 sp
    @CrossOrigin
    @PutMapping(value = "/adminPage/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") long productId,
                                                 @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                                 @RequestParam(value = "price", required = false, defaultValue = "") double price,
                                                 @RequestParam(value = "categoryId",  required = false, defaultValue = "") long categoryId,
                                                 @RequestParam(value = "description", required = false, defaultValue = "") String description,
                                                 @RequestParam(value = "preview", required = false, defaultValue = "") String preview,
                                                 @RequestParam(value = "image", required = false) MultipartFile file,
                                                 HttpServletRequest request) {
        if(jwtService.isAdmin(request)){
            try{
                Product currentProduct= productService.findById(productId);
                if(!currentProduct.isActive()){
                    return new ResponseEntity("Sản phẩm đã ngừng bán, vui lòng chọn sản phẩm khác!", HttpStatus.BAD_REQUEST);
                }
                if(name!=""){
                    currentProduct.setName(name);
                }
                if(preview!=""){
                    currentProduct.setPreview(preview);
                }
                if(String.valueOf(price)!=""){
                    currentProduct.setPrice(price);
                }
                if(String.valueOf(categoryId)!=""){
                    currentProduct.setCategory(categoryService.findById(categoryId));
                }
                if(description!=""){
                    currentProduct.setDescription(description);
                }
                if(file!= null){
                    filesStorageService.save(file);
                    String imageName= file.getOriginalFilename();
                    Path path = Paths.get(fileDir);
                    Path resolvedPath
                            = path.resolve(imageName);
                    System.out.println(resolvedPath.toString());
                    currentProduct.setImage("http://localhost:"+ String.valueOf(port)+resolvedPath);
                    currentProduct.setImage(fileDir+ file.getOriginalFilename());
                }
                productService.save(currentProduct);
                return new ResponseEntity<>(currentProduct, HttpStatus.OK);
            }
            catch (Exception e){
                return new ResponseEntity("Sản phẩm không tồn tại, vui lòng chọn sản phẩm khác", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity("Bạn không phải là admin", HttpStatus.METHOD_NOT_ALLOWED);
    }


    // xóa 1 sản phẩm
    @CrossOrigin
    @DeleteMapping(value = "/adminPage/products/{id}")
    public ResponseEntity<Product> stopSelling(@PathVariable("id") long productId,
                                                 HttpServletRequest request) {
        if(jwtService.isAdmin(request)){
            try{
                Product product = productService.findById(productId);
                product.setActive(false);
                productService.save(product);
                return new ResponseEntity("Xóa sản phẩm thành công!", HttpStatus.OK);
            }
            catch (Exception e){
                logger.error(e.getMessage());
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity("Bạn không phải là admin", HttpStatus.METHOD_NOT_ALLOWED);
    }

    public static void main(String[] args) {
        String url= "http://localhost:8081/uploads/Screenshot%20from%202020-12-24%2000-22-58.png";
        Path path = Paths.get(url);
        Path resolvedPath
                = path.resolve(path);
        System.out.println(resolvedPath);
        System.out.println(System.getProperty("user.dir"));
    }
}

