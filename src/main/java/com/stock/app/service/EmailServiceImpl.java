package com.stock.app.service;


import com.stock.app.constants.Constants;

import com.stock.app.model.RuleEntity;
import com.stock.app.model.StockDto;
import com.stock.app.model.UserEntity;
import com.stock.app.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * Email service class used to construct and send user email notifications.
 *
 * @author rolland.schnell
 */
@Component
public class EmailServiceImpl implements EmailService {

    private final static Logger Log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private JavaMailSender emailSender;

    private RuleManagerService ruleManagerService;

    private StockManagerService stockManagerService;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender, RuleManagerService ruleManagerService,
                            StockManagerService stockManagerService) {
        this.emailSender = emailSender;
        this.ruleManagerService = ruleManagerService;
        this.stockManagerService = stockManagerService;
    }

    /**
     * Send MIME type mail to users who has ruled defined an met the min stock price condition.
     * @param recipient - the registered user's email
     * @param subject - email subject
     * @param body - email HTML body
     * @throws MessagingException - if mail could not be sent
     */
    @Override
    public void sendEmail(String recipient, String subject, String body) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.setContent(body, "text/html; charset=utf-8");
        message.setRecipients(Message.RecipientType.TO,  recipient);
        message.setSubject(subject);
        emailSender.send(message);
    }

    /**
     * Check if there are any rules in the db if so map through and if the min stock price condition is met initiate the
     * email construction and sending process. For reference stock value the lower end value of the specific stock for the
     * current day was taken in consideration.
     * @throws Exception
     */
    @Override
    public void sendUserStockNotification() throws Exception {

        List<RuleEntity> userRuleEntities = ruleManagerService.getAllRules();

        userRuleEntities.forEach(rule -> {
            try {
                StockDto stock = stockManagerService.getStock(rule.getStockName());
                if (stock.getLow() < rule.getMinStockValue()) {
                    Log.info("[{}] Sending email to user {}", DateUtils.getTimestamp(), rule.getUserEntity().getFirstName());

                    composeAndSendEmail(rule, stock);

                    Log.info("[{}] Email sent!", DateUtils.getTimestamp());

                }
            } catch (Exception ex) {
                Log.error("Failed to send user notification");
                ex.printStackTrace();
            }
        });
    }

    /**
     * Compose the email for the user that defined a specific ruleEntity having the stock information for reference values.
     * @param ruleEntity - UserEntity defined ruleEntity
     * @param stock - The stock object that contains the data requested by the ruleEntity stockName.
     * @throws MessagingException
     */
    private void composeAndSendEmail(RuleEntity ruleEntity, StockDto stock) throws MessagingException {
        UserEntity userEntity = ruleEntity.getUserEntity();
        String recipient = userEntity.getEmail();
        String subject = Constants.GENERIC_EMAIL_SUBJECT + " " + stock.getSymbol();
        StringBuilder body = new StringBuilder();

        body.append("<div>")
                .append("<h3>").append("Dear userEntity ").append(userEntity.getFirstName()).append("</h3>")
                .append("<br>")
                .append("The price for the stock ")
                .append("<b>").append(ruleEntity.getStockName()).append("</b>")
                .append(" has dropped below the value set ")
                .append("<b>").append(ruleEntity.getMinStockValue()).append("</b>")
                .append(" and has a current value of ")
                .append("<b>").append(stock.getLow()).append("</b>")
                .append("<div>");


        sendEmail(recipient, subject, body.toString());
    }

}