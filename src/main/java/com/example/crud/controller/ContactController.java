package com.example.crud.controller;

import com.example.crud.input.ContactForm;
import com.example.crud.service.SendEmailService;
import org.apache.commons.mail.EmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
public class ContactController {
    private SendEmailService emailService;

    public ContactController(SendEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping(value = "/userPage/contact")
    public ResponseEntity<String> contactToAdmin(@Valid @RequestBody ContactForm contactForm) throws EmailException {

        StringBuilder sb = new StringBuilder("From" + contactForm.getFullName() + "\n");
        sb.append("Address: " + contactForm.getEmail() + "\n");
        sb.append("Content: " + contactForm.getContent());

        if (emailService.contactToAdmin(sb.toString())) {
            return new ResponseEntity<>("Hãy để ý email của bạn, admin sẽ phản hổi trong vòng 1 ngày", HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
