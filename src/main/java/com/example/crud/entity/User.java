package com.example.crud.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.aspectj.weaver.ast.Or;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
    created by HuyenNgTn on 15/11/2020
*/
@Entity
@Table(name = "tblUSERS")
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long userId;

    @Column(name = "user_name", unique = true)
    @NotEmpty(message = "*Please provide your username")
    private String userName;

    @Column(name = "full_name")
    @NotEmpty(message = "*Please provide your full name")
    private String fullName;

    @Length(min=5, message = "Your password must have at least 5 character")
    @NotEmpty(message = "*Please provide your password")
    @Column(name = "pass_word")
    protected String password;

    @Email(message = "*Please provide a valid email")
    @NotEmpty(message = "Please provide an email")
    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    protected String phone;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name ="last_active", nullable = false)
    private long lastActive;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "enable")
    private boolean enable;


    public User(){

    }

    public User(long userId, String userName, String fullName, String password, String email, String phone, String role, String avatar) {
        this.userId = userId;
        this.userName = userName;
        this.fullName= fullName;
        this.password= password;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.avatar= avatar;
        this.enable= true;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getLastActive() {
        return lastActive;
    }

    public void setLastActive(long lastActive) {
        this.lastActive = lastActive;
    }

        public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    //    public List<GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        for (String role : roles) {
//            authorities.add(new SimpleGrantedAuthority(role));
//        }
//        return authorities;
//    }
}
