package com.stock.app.service;

import com.stock.app.dao.RuleDAO;
import com.stock.app.dao.UserDAO;
import com.stock.app.exceptions.RuleAlreadyExistsException;
import com.stock.app.model.Rule;
import com.stock.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleManagerServiceImpl implements RuleManagerService{

    private RuleDAO ruleDAO;

    private UserDAO userDAO;

    @Autowired
    public RuleManagerServiceImpl(RuleDAO ruleDAO, UserDAO userDAO) {
        this.ruleDAO = ruleDAO;
        this.userDAO = userDAO;
    }

    @Override
    public void createNewRule(Rule rule, String email) throws RuleAlreadyExistsException {

        try {
            User user = userDAO.getUserByEmail(email);

            if (user != null) {
                Rule ruleFromDb = ruleDAO.getRuleByStockNameAndUser(rule.getStockName(), user);

                if (ruleFromDb != null) {
                    throw new RuleAlreadyExistsException("Rule already exists for " +  rule.getStockName());
                } else {
                    rule.setUser(user);
                    ruleDAO.save(rule);
                }

            }

        } catch (Exception ex) {
            throw new RuleAlreadyExistsException("Could not save rule " + rule.toString());
        }
    }

    @Override
    public List<Rule> getRulesByUser(String email) throws Exception {

        try {
            User user = userDAO.getUserByEmail(email);

            if (user != null) {
                List<Rule> userRules = ruleDAO.getRulesByUser(user);
                return userRules;
            } else {
                throw new Exception("User not found to retrieve rules from");
            }
        } catch(Exception ex) {
            throw new Exception("Cannot retrieve user rules");
        }
    }

    @Override
    public Rule updateRuleByUserEmailAndRule(Rule rule, String email) {
        return null;
    }

    @Override
    public boolean deleteRuleByRuleId(Long ruleId) {
        return false;
    }
}
