package com.stock.app.controller;

import com.stock.app.exceptions.RuleAlreadyExistsException;
import com.stock.app.exceptions.RuleNotFoundException;
import com.stock.app.exceptions.UserNotFoundException;
import com.stock.app.model.RuleEntity;
import com.stock.app.model.StockDto;
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

/**
 * Main controller for the stock API, controls all REST calls related to user rule management and stock related external
 * calls
 *
 * @author rolland.schnell
 */
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

    /**
     * Returns all user rules for a specific user given by it's unique registered email.
     * @param email - unique user identifier
     * @return - the user roles if OK else error message
     */
    @RequestMapping(value = "/getRulesByUser", method = RequestMethod.GET)
    public ResponseEntity getRulesByUser(@RequestParam(value="email") String email) {

        Log.info("[{}] Get stock rule for {}", DateUtils.getTimestamp(), email);

        List<RuleEntity> userRuleEntities;

        try {
            userRuleEntities = ruleManagerService.getRulesByUser(email);
        } catch(UserNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(userRuleEntities, HttpStatus.OK);
    }

    /**
     * Saves a new user ruleEntity linked to user by it's unique email if not exists else an error message is returned.
     * @param ruleEntity - the actual user ruleEntity to be saved
     * @param email - the email for user identification
     * @return - the saved ruleEntity or error message if user not found or if ruleEntity exists.
     */
    public @ResponseBody
    @RequestMapping(value = "/addNewRule", method = RequestMethod.POST)
    ResponseEntity addNewRule(@RequestBody RuleEntity ruleEntity, @RequestParam String email) {

        Log.info("[{}] Creating new ruleEntity for user {} having stock name {} and min value {}", DateUtils.getTimestamp(),
                email, ruleEntity.getStockName(), ruleEntity.getMinStockValue());

        try {
            ruleManagerService.createNewRule(ruleEntity, email);
        } catch(RuleAlreadyExistsException | UserNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ruleEntity, HttpStatus.CREATED);
    }

    /**
     * Update existing user ruleEntity with new stock symbol and / or min stock value for email trigger.
     * @param ruleEntity - the new user ruleEntity
     * @param email - user identification email
     * @return - the user ruleEntity or error if the user / ruleEntity is not found or other exception happens.
     */
    public @ResponseBody
    @RequestMapping(value = "/updateRule", method = RequestMethod.PUT)
    ResponseEntity updateRule(@RequestBody RuleEntity ruleEntity, String email) {

        Log.info("[{}] Updating ruleEntity for user {} having stock name {} and min value {}", DateUtils.getTimestamp(),
                email, ruleEntity.getStockName(), ruleEntity.getMinStockValue());

        RuleEntity updatedRuleEntity;

        try {
            updatedRuleEntity = ruleManagerService.updateUserRule(ruleEntity, email);
        } catch(RuleNotFoundException | UserNotFoundException ex ) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(updatedRuleEntity, HttpStatus.CREATED);
    }


    /**
     * Return the stock object created by calling the external stock API provider by a given stock symbol.
     * @param symbol - the stock symbol requested
     * @return the created stock
     */
    public @ResponseBody
    @RequestMapping(value = "/getStock", method = RequestMethod.GET)
    ResponseEntity getStock(@RequestParam String symbol) {

        Log.info("[{}] get stocks for {}", DateUtils.getTimestamp(), symbol);

        StockDto stock;

        try {
            stock = stockManagerService.getStock(symbol);
        } catch(Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(stock, HttpStatus.OK);
    }

    /**
     * Returns all user rules in the db regardless the user.
     * @return - array of user rules
     */
    public @ResponseBody
    @RequestMapping(value = "/getUserRules", method = RequestMethod.GET)
    ResponseEntity getStock() {

        Log.info("[{}] get all user rules", DateUtils.getTimestamp());

        List<RuleEntity> userRuleEntities;

        try {
            userRuleEntities = ruleManagerService.getAllRules();
        } catch(Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(userRuleEntities, HttpStatus.OK);
    }
}
