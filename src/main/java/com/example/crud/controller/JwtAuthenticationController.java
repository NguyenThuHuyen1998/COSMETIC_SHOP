package com.example.crud.controller;

import com.example.crud.config.JwtTokenUtil;
import com.example.crud.constants.InputParam;
import com.example.crud.entity.Cart;
import com.example.crud.entity.CustomUserDetails;
import com.example.crud.entity.User;
import com.example.crud.input.JwtRequest;
import com.example.crud.output.JwtResponse;
import com.example.crud.service.CartService;
import com.example.crud.service.JwtService;
import com.example.crud.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import com.example.crud.service.impl.JwtUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@RestController
@CrossOrigin
public class JwtAuthenticationController {
    public static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

    @Value("${file.upload-dir}")
    private String fileDir;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<User> saveUser(@RequestBody User user) throws Exception {
        User currentUser = userService.findByName(user.getUserName());
        if (currentUser != null) {
            return new ResponseEntity("Username đã tồn tại", HttpStatus.BAD_REQUEST);
        }
        String email = user.getEmail();
        if (!EmailValidator.getInstance().isValid(email)) {
            return new ResponseEntity("Email không hợp lệ!", HttpStatus.BAD_REQUEST);
        }
        user.setRole(InputParam.USER);
        user.setAvatar("avatardefault.png");
        user.setEnable(true);
        //set lần cuối hoạt động để check sau 3 tháng k hoạt động thì xóa sản phẩm trong giỏ hàng
        user.setLastActive(new Date().getTime());
        userDetailsService.save(user);
        Cart cart = new Cart(user);
        cartService.save(cart);
        return ResponseEntity.ok(user);

    }

    @RequestMapping(value = "/logout", produces = "application/json", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<User> logout(HttpServletRequest request, HttpServletResponse response) {
//		if(jwtService.isUser(request)){
//			return new ResponseEntity<>(HttpStatus.OK);
//		}
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ResponseEntity("Đăng nhập trước khi thực hiện", HttpStatus.BAD_REQUEST);

    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
