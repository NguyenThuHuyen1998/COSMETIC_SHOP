package com.example.crud.controller;

import com.example.crud.entity.Address;
import com.example.crud.entity.Order;
import com.example.crud.input.ChangePasswordForm;
import com.example.crud.input.ConfirmPassword;
import com.example.crud.input.UserForm;
import com.example.crud.predicate.PredicateOrderFilter;
import com.example.crud.service.*;
import com.example.crud.service.impl.JwtServiceImpl;
import com.example.crud.constants.InputParam;
import com.example.crud.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.SecureRandom;
import java.util.List;
import java.util.function.Predicate;

/*

    created by HuyenNgTn on 15/11/2020
*/
@RestController
public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private JwtServiceImpl jwtHandler;
    private UserService userService;
    private OrderService orderService;
    private SendEmailService sendEmailService;
    private ShipService shipService;
    private PasswordEncoder passwordEncoder;
    private FilesStorageService filesStorageService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Value("${file.upload-dir}")
    private String fileDir;

    @Autowired
    public UserController(UserService userService,
                          OrderService orderService,
                          JwtServiceImpl jwtHandler,
                          SendEmailService service,
                          ShipService shipService,
                          FilesStorageService filesStorageService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.orderService = orderService;
        this.jwtHandler = jwtHandler;
        this.sendEmailService= service;
        this.shipService= shipService;
        this.passwordEncoder = passwordEncoder;
    }

    // xem thông tin cá nhân user
    @RequestMapping(value = "/userPage/users", method = RequestMethod.GET)
    public ResponseEntity<Object> getDetailUser(HttpServletRequest request) {
        if (jwtHandler.isUser(request)) {
            long userId = jwtHandler.getCurrentUser(request).getUserId();
            User user = userService.findById(userId);
            return new ResponseEntity<Object>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>("Đăng nhập trước khi thực hiện", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PutMapping(value = "/userPage/users")
    public ResponseEntity<User> editUser(@RequestParam("fullName") String fullName,
                                         @RequestParam("email") String email,
                                         @RequestParam("phone") String phone,
                                         @RequestParam("avatar") MultipartFile avatar,
                                       HttpServletRequest request) {
        if (jwtHandler.isUser(request)) {
            try {
                User user = jwtHandler.getCurrentUser(request);
                user.setFullName(fullName);
                user.setPhone(phone);
                user.setEmail(email);
                filesStorageService.save(avatar);
                user.setAvatar(fileDir+ avatar.getOriginalFilename());
                userService.add(user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } catch (Exception e) {
                logger.error(e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity("Đăng nhập trước khi thực hiện", HttpStatus.METHOD_NOT_ALLOWED);
    }

    //Thay đổi mật khẩu người dùng
    @PutMapping(value = "/user/changePassword")
    public ResponseEntity<User> changePassword(@RequestBody ChangePasswordForm data, HttpServletRequest request) {
        if (jwtHandler.isUser(request)) {
            try {
                long userId = jwtHandler.getCurrentUser(request).getUserId();
                User user = userService.findById(userId);
                passwordEncoder = new BCryptPasswordEncoder();
                if (userService.changePassword(user, data.getOldPass(), data.getNewPass())) {
                    return new ResponseEntity("Success", HttpStatus.OK);
                }
                return new ResponseEntity("Password is wrong", HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity("Đăng nhập trước khi thực hiện", HttpStatus.METHOD_NOT_ALLOWED);
    }


    @PostMapping(value = "/user/forgetPassword")
    public ResponseEntity<User> forgetPassword(@RequestParam(name = "userName") String userName) {
//        try{
        User user = userService.findByName(userName);
        if (user == null) {
            return new ResponseEntity("Username is not exist", HttpStatus.BAD_REQUEST);
        }
        String randomStr= userService.forgetPassword(user);
        String message = "Confirm code of account "+ userName+ " is: " + randomStr;
        if (!sendEmailService.resetPassword(message, user.getEmail())) {
            return new ResponseEntity("Can't send reset password for you", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Confirm code sent to your email.", HttpStatus.OK);
    }

    @PostMapping(value = "/user/confirmPassword")
    public ResponseEntity<User> confirmPassword(@RequestBody ConfirmPassword confirmPassword,
                                                HttpServletRequest request){
        String userName= request.getHeader(InputParam.USER_NAME);
        String confirmCode= confirmPassword.getConfirmCode();
        String newPassword= confirmPassword.getNewPassword();
        User user= userService.findByName(userName);
        if(user== null){
            return new ResponseEntity("User not found", HttpStatus.BAD_REQUEST);
        }
        if(userService.changePassword(user, confirmCode, newPassword)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity("Confirm code is wrong", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/userPage/addressShip")
    public ResponseEntity<Address> getListAddressOfUser(HttpServletRequest request){
        if(jwtHandler.isCustomer(request)){
            long userId= jwtHandler.getCurrentUser(request).getUserId();
            List<Address> list= shipService.getListAddressOfUser(userId);
            if (list== null || list.size()==0){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(list, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PostMapping(value = "/userPage/addressShip")
    public ResponseEntity<Address> createShipAddress(@RequestBody Address address,
                                                        HttpServletRequest request){
        if(jwtHandler.isCustomer(request)){
            User user= jwtHandler.getCurrentUser(request);
            long userId= jwtHandler.getCurrentUser(request).getUserId();
            List<Address> list= shipService.getListAddressOfUser(userId);
            if (list.size() >4){
                return new ResponseEntity("Đã quá số lượng địa chỉ mối người, bạn không thể thêm mới!", HttpStatus.BAD_REQUEST);
            }
            address.setUser(user);
            shipService.save(address);
            return new ResponseEntity(address, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PutMapping(value = "/userPage/addressShip/{id}")
    public ResponseEntity<Address> editAddress(@PathVariable("id") long id,
                                                     @RequestBody Address address,
                                                     HttpServletRequest request){
        if(jwtHandler.isCustomer(request)){
            User user= jwtHandler.getCurrentUser(request);
            if (address== null || address.getUser().getUserId()!= user.getUserId()|| address.getAddressId()!= id){
                return new ResponseEntity("Địa chỉ không hợp lệ!", HttpStatus.BAD_REQUEST);
            }
            shipService.save(address);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @DeleteMapping(value = "/userPage/addressShip/{id}")
    public ResponseEntity<Address> deleteAddress(@PathVariable("id") long id,
                                               HttpServletRequest request){
        if(jwtHandler.isCustomer(request)){
            User user= jwtHandler.getCurrentUser(request);
            Address address= shipService.getAddress(id);
            if (address== null || address.getUser()!= user){
                return new ResponseEntity("Địa chỉ không hợp lệ!", HttpStatus.BAD_REQUEST);
            }
            shipService.delete(address);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    //-------------------------------------------ADMIN---------------------------------------------
    //xem danh sách user hiện có và trạng thái hoạt động
    @GetMapping(value = "/adminPage/users")
    public ResponseEntity<User> getAllUser(HttpServletRequest request) {
        if (jwtHandler.isAdmin(request)) {
            List<User> userList = userService.findAllUser();
            if (userList == null || userList.size() == 0) {
                logger.error("User list empty");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(userList, HttpStatus.OK);
        }
        return new ResponseEntity("You isn't admin", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @DeleteMapping(value = "/adminPage/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable(name = "id") long userId, HttpServletRequest request) {
        if (jwtHandler.isAdmin(request)) {
            User user = userService.findById(userId);
            Predicate<Order> predicate = null;
            PredicateOrderFilter predicateOrderFilter = PredicateOrderFilter.getInstance();
            Predicate<Order> checkUser = predicateOrderFilter.checkUser(userId);
            Predicate<Order> checkStatusProcessing = predicateOrderFilter.checkStatus(InputParam.PROCESSING);
            predicate = checkUser.and(checkStatusProcessing);
            List<Order> orderList = predicateOrderFilter.filterOrder(orderService.findAllOrder(), predicate);
            for (Order order : orderList) {
                orderService.remove(order);
            }

            userService.delete(user);
            return new ResponseEntity("Success", HttpStatus.OK);
        }
        return new ResponseEntity("You isn't admin", HttpStatus.METHOD_NOT_ALLOWED);
    }

    /* ---------------- không xóa user, chỉ vô hiệu hóa ------------------------ ADMIN*/
//    @RequestMapping(value = "/adminPage/users/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<String> disableUser(@PathVariable("id") long id) {
//        User user= userService.findById(id);
//        if(user!= null){
//            userService.add(user);
//            return new ResponseEntity<String>("Disable!", HttpStatus.OK);
//        }
//        return new ResponseEntity<>("User not exist", HttpStatus.BAD_REQUEST);
//
//    }


    // phân quyền user làm admin
    @PostMapping(value = "/adminPage/decentralization/{id}")
    public ResponseEntity<User> decentralizationAdmin(@PathVariable("id") long id, HttpServletRequest request) {
        if (jwtHandler.isAdmin(request)) {
            User user = userService.findById(id);
            if (user == null) {
                return new ResponseEntity("Người dùng không tồn tại", HttpStatus.BAD_REQUEST);
            } else {
                user.setRole(InputParam.ADMIN);
                userService.update(user);
                return new ResponseEntity(user, HttpStatus.OK);
            }
        }
        return new ResponseEntity("Bạn không phải là admin", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PutMapping(value = "/adminPage/lockUser/{id}")
    public ResponseEntity<User> lockUser(@PathVariable("id") long userId,
                                         HttpServletRequest request){
        if (jwtHandler.isAdmin(request)) {
            User user = userService.findById(userId);
            if (user == null) {
                return new ResponseEntity("Người dùng không tồn tại", HttpStatus.BAD_REQUEST);
            } else {
                user.setEnable(false);
                userService.update(user);
                return new ResponseEntity(user, HttpStatus.OK);
            }
        }
        return new ResponseEntity("Bạn không phải là admin", HttpStatus.METHOD_NOT_ALLOWED);
    }
}

