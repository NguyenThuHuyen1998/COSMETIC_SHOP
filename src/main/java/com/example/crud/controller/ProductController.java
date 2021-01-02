package com.example.crud.controller;

import com.example.crud.constants.InputParam;
import com.example.crud.entity.Category;
import com.example.crud.entity.Product;
import com.example.crud.service.CategoryService;
import com.example.crud.service.FilesStorageService;
import com.example.crud.service.JwtService;
import com.example.crud.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

@RestController
public class ProductController {
    public static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;
    private final CategoryService categoryService;
    private final JwtService jwtService;
    private FilesStorageService filesStorageService;

    @Value("${file.upload-dir}")
    private String fileDir;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, JwtService jwtService, FilesStorageService filesStorageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.jwtService= jwtService;
        this.filesStorageService= filesStorageService;
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
                                         @RequestParam(required = false, defaultValue = "") String sortBy,
                                         @RequestParam(required = false, defaultValue = "9") int limit,
                                         @RequestParam(required = false, defaultValue = "1") int page) throws ParseException {
            Map<String, Object> input = new HashMap<>();
            input.put(InputParam.KEY_WORD, keyword);
            input.put(InputParam.PRICE_MAX, priceMax);
            input.put(InputParam.PRICE_MIN, priceMin);
            input.put(InputParam.CATEGORY_ID, categoryId);
            input.put(InputParam.SORT_BY, sortBy);
            List<Product> output = productService.filterProduct(input);

            List<Product> totalProduct= productService.findAllProduct();
            int totalPage = (output.size()) / limit + ((output.size() % limit == 0) ? 0 : 1);
            int total_count=output.size();
            Map<String, Object> paging= new HashMap<>();
            paging.put(InputParam.RECORD_IN_PAGE, limit);
            paging.put(InputParam.TOTAL_COUNT, total_count);
            paging.put(InputParam.CURRENT_PAGE, page);
            paging.put(InputParam.TOTAL_PAGE, totalPage);
            Map<String, Object> result= new HashMap<>();
            result.put(InputParam.PAGING, paging);
            result.put(InputParam.DATA, output);
            return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping(value = "/products/bestSeller")
    public ResponseEntity<Product> getListProductBestSeller(){
        return null;
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
                filesStorageService.save(file);
                product.setImage(fileDir+ file.getOriginalFilename());
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
                return new ResponseEntity("Success", HttpStatus.OK);
            }
            catch (Exception e){
                logger.error(e.getMessage());
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity("Bạn không phải là admin", HttpStatus.METHOD_NOT_ALLOWED);
    }
}
