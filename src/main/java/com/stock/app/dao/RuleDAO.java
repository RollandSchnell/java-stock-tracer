package com.stock.app.dao;

import com.stock.app.exceptions.RuleAlreadyExistsException;
import com.stock.app.exceptions.UserNotFoundException;
import com.stock.app.model.RuleEntity;
import com.stock.app.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface RuleDAO extends CrudRepository<RuleEntity, Long> {

    RuleEntity getRuleEntityByStockNameAndUserEntity(String stockName, UserEntity userEntity) throws RuleAlreadyExistsException;

    List<RuleEntity> getRulesByUserEntity(UserEntity userEntity) throws UserNotFoundException;

    List<RuleEntity> findAll();
}
