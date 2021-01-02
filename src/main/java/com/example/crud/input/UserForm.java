package com.example.crud.input;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;

@Validated
public class UserForm {
    private long userId;
    private String fullName;

    @Email
    private String email;

    private String address;

    private String phone;

    public UserForm(long userId, String fullName, @Email String email, String address, String phone) {
        this.userId= userId;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
