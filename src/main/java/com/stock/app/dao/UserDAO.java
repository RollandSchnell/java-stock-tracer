package com.stock.app.dao;

import com.stock.app.exceptions.UserNotFoundException;
import com.stock.app.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserDAO extends CrudRepository<UserEntity, Long> {

    UserEntity getUserEntitiesByEmail(String email) throws UserNotFoundException;

    List<UserEntity> findAll();
}
