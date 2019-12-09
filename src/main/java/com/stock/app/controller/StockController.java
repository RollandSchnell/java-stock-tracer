package com.stock.app.controller;

import com.stock.app.exceptions.RuleAlreadyExistsException;
import com.stock.app.model.Rule;
import com.stock.app.model.Stock;
import com.stock.app.service.RuleManagerService;
import com.stock.app.service.StockManagerService;
import com.stock.app.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stockApi")
public class StockController {

    private final static Logger Log = LoggerFactory.getLogger(StockController.class);

    private RuleManagerService ruleManagerService;

    private StockManagerService stockManagerService;

    @Autowired
    public StockController(RuleManagerService ruleManagerService, StockManagerService stockManagerService) {
        this.ruleManagerService = ruleManagerService;
        this.stockManagerService = stockManagerService;
    }

    @RequestMapping(value = "/getRulesByUser", method = RequestMethod.GET)
    public ResponseEntity getRulesByUser(@RequestParam(value="email") String email) {

        Log.info("[{}] Get stock rule for {}", DateUtils.getTimestamp(), email);

        List<Rule> userRules;

        try {
            userRules = ruleManagerService.getRulesByUser(email);
        } catch(Exception e) {
            return new ResponseEntity<>("There was an error retrieving the user rules", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userRules, HttpStatus.OK);
    }

    public @ResponseBody
    @RequestMapping(value = "/addNewRule", method = RequestMethod.POST)
    ResponseEntity addNewRule(@RequestBody Rule rule, @RequestParam String email) {

        Log.info("[{}] Creating new rule for user {} having stock name {} and min value {}", DateUtils.getTimestamp(),
                email, rule.getStockName(), rule.getMinStockValue());

        try {
            ruleManagerService.createNewRule(rule, email);
        } catch(RuleAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(rule, HttpStatus.CREATED);
    }

    public @ResponseBody
    @RequestMapping(value = "/updateRule", method = RequestMethod.POST)
    ResponseEntity updateRule(@RequestBody Rule rule, String email) {

        Log.info("[{}] Updating rule for user {} having stock name {} and min value {}", DateUtils.getTimestamp(),
                email, rule.getStockName(), rule.getMinStockValue());

        Rule updatedRule;

        try {
            updatedRule = ruleManagerService.updateRuleByUserEmailAndRule(rule, email);
        } catch(RuleAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(updatedRule, HttpStatus.CREATED);
    }


    public @ResponseBody
    @RequestMapping(value = "/getStock", method = RequestMethod.GET)
    ResponseEntity getStock(@RequestParam String symbol) {

        Log.info("[{}] get stocks for {}", DateUtils.getTimestamp(), symbol);

        Stock stock;

        try {
            stock = stockManagerService.getStock(symbol);
        } catch(Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(stock, HttpStatus.OK);
    }


}
