package com.stock.app.service;

import com.stock.app.dao.UserDAO;
import com.stock.app.exceptions.UserAlreadyExistsException;
import com.stock.app.exceptions.UserNotFoundException;
import com.stock.app.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final static Logger Log = LoggerFactory.getLogger(UserManagementServiceImpl.class);

    private UserDAO userDAO;

    @Autowired
    public UserManagementServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void checkLoginDetails(String email, String password) throws UserNotFoundException {
        try {
            User user = userDAO.getUserByEmail(email);
            if (user != null && user.getEmail().equals(email) && user.getPassword().equals(password)) {
                Log.info("User credentials OK");
            } else {
                throw new UserNotFoundException("Invalid login credentials");
            }
        } catch (Exception e) {
            throw new UserNotFoundException("Invalid login credentials");
        }

    }

    @Override
    public void registerNewUser(User user) throws UserAlreadyExistsException {
        try {
            // check if exists
            User userFromDb = userDAO.getUserByEmail(user.getEmail());

            // save if not
            if (userFromDb == null) {
                userDAO.save(user);
            } else {
                throw new UserAlreadyExistsException("User already exists");
            }
        } catch (Exception e) {
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    @Override
    public List<User> getAllUsers() throws Exception {
        try {
            return userDAO.findAll();
        } catch(Exception e) {
            throw e;
        }
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException {
        try {
            // check if exists
            User userFromDb = userDAO.getUserByEmail(user.getEmail());

            if (userFromDb == null) {
                throw new UserNotFoundException("User not found");
            } else {
                userFromDb.setEmail(user.getEmail());
                userFromDb.setFirstName(user.getFirstName());
                userFromDb.setLastName((user.getLastName()));
                userFromDb.setPassword(user.getPassword());
                //userFromDb.setRules(user.getRules());
                userDAO.save(userFromDb);

                return userFromDb;
            }
        } catch (Exception e) {
            throw new UserNotFoundException("User not found");
        }
    }

}
