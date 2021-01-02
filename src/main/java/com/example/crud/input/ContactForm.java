package com.example.crud.input;

import javax.validation.constraints.NotNull;

public class ContactForm {

    @NotNull
    private String fullName;
    @NotNull
    private String email;
    @NotNull
    private String content;
    private String phone;

    public ContactForm(String fullName, @NotNull String email, @NotNull String content, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.content = content;
        this.phone = phone;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
