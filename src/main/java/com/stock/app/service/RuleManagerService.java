package com.stock.app.service;

import com.stock.app.exceptions.RuleAlreadyExistsException;
import com.stock.app.exceptions.RuleNotFoundException;
import com.stock.app.exceptions.UserNotFoundException;
import com.stock.app.model.RuleEntity;

import java.util.List;

public interface RuleManagerService {

    void createNewRule(RuleEntity ruleEntity, String email) throws RuleAlreadyExistsException, UserNotFoundException;

    List<RuleEntity> getRulesByUser(String email) throws UserNotFoundException, Exception;

    List<RuleEntity> getAllRules() throws Exception;

    RuleEntity updateUserRule(RuleEntity ruleEntity, String email) throws UserNotFoundException, RuleNotFoundException, Exception;

}
