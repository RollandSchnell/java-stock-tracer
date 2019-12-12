package com.stock.app.schedule;

import com.stock.app.model.RuleEntity;
import com.stock.app.service.EmailService;
import com.stock.app.service.RuleManagerService;
import com.stock.app.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Scheduled Task creator class used to define and implement the scheduled task that calls the external Stock API for
 * stock information regarding user defined rules. The task will determine if the returned value is below a specified
 * rule min stock value and sent email notifications to selected rule users.
 *
 * @author rolland.schnell
 */
@Component
public class StockScheduler {

    private final static Logger Log = LoggerFactory.getLogger(StockScheduler.class);

    private RuleManagerService ruleManagerService;

    private EmailService emailService;

    public StockScheduler(RuleManagerService ruleManagerService, EmailService emailService) {
        this.ruleManagerService = ruleManagerService;
        this.emailService = emailService;
    }

    /**
     * Executes the scheduled task every X seconds defined in the application.properties file. Having a initial delay for
     * app setup after what the fixedDelay as the user prefers. Checks if the are any user defined rules in the db if so
     * an external stockAPI is called and the rules evaluated against the returned values.
     */
    @Scheduled(fixedDelayString = "${stock.schedule.delay.milliseconds}",
            initialDelayString = "${stock.schedule.initialDelay.milliseconds}")
    public void executeTask() {
        Log.info("[{}] Executing scheduled task...", DateUtils.getTimestamp());

        try {
            List<RuleEntity> userRuleEntities = ruleManagerService.getAllRules();

            if (userRuleEntities != null && userRuleEntities.size() != 0) {
                emailService.sendUserStockNotification();
            } else {
                Log.info("[{}] There are no rules available", DateUtils.getTimestamp());
            }
        } catch (Exception ex) {
            Log.error("Could not send user notifications");
        }
    }

}
