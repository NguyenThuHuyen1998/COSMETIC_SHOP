package com.example.crud.input;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ConfirmPassword {
    @NotEmpty(message = "*Please provide your confirm code")
    private String confirmCode;
    @Length(min = 5)
    @NotEmpty(message = "*Please pro")
    private String newPassword;

    public ConfirmPassword(String confirmCode, String newPassword) {
        this.confirmCode = confirmCode;
        this.newPassword = newPassword;
    }


    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
