package com.example.crud.service.impl;

import com.example.crud.config.JwtTokenUtil;
import com.example.crud.constants.InputParam;
import com.example.crud.entity.User;
import com.example.crud.service.JwtService;
import com.example.crud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/*
    created by HuyenNgTn on 10/12/2020
*/
@Service
public class JwtServiceImpl implements JwtService{
    public static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);
    private UserService userService;
    private JwtTokenUtil jwtTokenUtil= new JwtTokenUtil( userService);


    @Autowired
    public JwtServiceImpl(UserService userService){
        this.userService= userService;
    }

    @Override
    public User getCurrentUser(HttpServletRequest request){
        String token= request.getHeader(InputParam.AUTHORIZATION);
        String userName= jwtTokenUtil.getUsernameFromToken(token);
        User user= userService.findByName(userName);
        return user;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        String token= request.getHeader(InputParam.AUTHORIZATION);
        String userName= jwtTokenUtil.getUsernameFromToken(token);
        User user= userService.findByName(userName);
        String role= user.getRole();
        if(role.equals(InputParam.ADMIN)) return true;
        return false;
    }

    @Override
    public boolean isUser(HttpServletRequest request) {
        String token= request.getHeader(InputParam.AUTHORIZATION);
        String userName= jwtTokenUtil.getUsernameFromToken(token);
        User user= userService.findByName(userName);
        String role= user.getRole();
        if(role.equals(InputParam.ADMIN) || role.equals(InputParam.USER)) return true;
        return false;
    }

    @Override
    public boolean isCustomer(HttpServletRequest request) {
        String token= request.getHeader(InputParam.AUTHORIZATION);
        String userName= jwtTokenUtil.getUsernameFromToken(token);
        User user= userService.findByName(userName);
        String role= user.getRole();
        if(role.equals(InputParam.USER) && user.isEnable()) return true;
        return false;
    }

    @Override
    public String getUserName(HttpServletRequest request) {
        String token= request.getHeader(InputParam.AUTHORIZATION);
        String userName= jwtTokenUtil.getUsernameFromToken(token);
        return userName;
    }
}
