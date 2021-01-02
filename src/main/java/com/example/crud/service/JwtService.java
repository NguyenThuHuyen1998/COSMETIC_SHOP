package com.example.crud.service;

import com.example.crud.entity.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

public interface JwtService {
    String getUserName(HttpServletRequest request);
    User getCurrentUser(HttpServletRequest request);
    boolean isAdmin(HttpServletRequest request);
    boolean isUser(HttpServletRequest request);
    boolean isCustomer(HttpServletRequest request);
}
