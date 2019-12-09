package com.stock.app.dao;

import com.stock.app.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserDAO extends CrudRepository<User, Long> {

    User getUserByEmail(String email);

    List<User> findAll();
}
