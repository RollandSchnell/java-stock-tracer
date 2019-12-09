package com.stock.app.dao;

import com.stock.app.exceptions.RuleAlreadyExistsException;
import com.stock.app.model.Rule;
import com.stock.app.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface RuleDAO extends CrudRepository<Rule, Long> {

    Rule getRuleByStockNameAndUser(String stockName, User user) throws RuleAlreadyExistsException;

    List<Rule> getRulesByUser(User user);
}
