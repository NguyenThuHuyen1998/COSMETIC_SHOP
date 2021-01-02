package com.example.crud.service.impl;

import com.example.crud.service.SendEmailService;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    public static final Logger logger = LoggerFactory.getLogger(SendEmailServiceImpl.class);

    @Value("${email.admin}")
    private String emailAdmin;

    @Value("${email.user}")
    private String emailUser;

    @Value("${email.user.password}")
    private String passEmailUser;

    @Value("${email.admin.password}")
    private String passEmailAdmin;

    public boolean sendMail(String subject, String message, String emailFrom, String passEmailFrom, String emailTo)  {
        try{
            Email email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator(emailFrom, passEmailFrom));
            email.setSSLOnConnect(true);
            email.setFrom(emailFrom);
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(emailTo);
            email.send();
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }

    }

    @Override
    public boolean contactToAdmin(String message) throws EmailException{
        String subject= "Contact from COSMETIC_SHOP";
        return sendMail(subject, message, emailUser, passEmailUser, emailAdmin);
    }

    @Override
    public boolean notifyOrder(String message, String emailCustomer) {
        String subject= "Đơn đang vận chuyển";
        return sendMail(subject, message, emailAdmin, passEmailAdmin, emailCustomer);
    }

    @Override
    public boolean resetPassword(String message, String emailCustomer) {
        String subject= "Cấp lại mật khẩu";
        return sendMail(subject, message, emailAdmin, passEmailAdmin, emailCustomer);
    }

}
