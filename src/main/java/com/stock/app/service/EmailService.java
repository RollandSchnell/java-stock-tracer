package com.stock.app.service;

import javax.mail.MessagingException;

public interface EmailService {

    void sendEmail(String recipient, String subject, String body) throws MessagingException;

    void sendUserStockNotification() throws Exception;

}
