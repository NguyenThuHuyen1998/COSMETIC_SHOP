package com.example.crud.service;

import org.apache.commons.mail.EmailException;

public interface SendEmailService {

    public boolean contactToAdmin(String message) throws EmailException;
    public boolean notifyOrder(String message, String emailCustomer);
    public boolean resetPassword(String message, String emailCustomer);
}
