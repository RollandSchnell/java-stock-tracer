package com.stock.app.service;

import com.stock.app.exceptions.RuleAlreadyExistsException;
import com.stock.app.model.Rule;

import java.util.List;

public interface RuleManagerService {

    void createNewRule(Rule rule, String email) throws RuleAlreadyExistsException;

    List<Rule> getRulesByUser(String email) throws Exception;

    Rule updateRuleByUserEmailAndRule(Rule rule, String email) throws RuleAlreadyExistsException;

    boolean deleteRuleByRuleId(Long ruleId);

}
