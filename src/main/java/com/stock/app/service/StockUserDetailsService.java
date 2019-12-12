package com.stock.app.service;

import com.stock.app.dao.UserDAO;
import com.stock.app.exceptions.UserNotFoundException;
import com.stock.app.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * The custom user details class that is used to load and create a principal user from an existing user in the db.
 *
 * @author rolland.schnell
 */
@Service
public class StockUserDetailsService implements UserDetailsService {

    private UserDAO userDAO;

    @Autowired
    public StockUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Load the auth User object using UserDetails as config, by requesting an existing user from db by userEmail ( the username ).
     *
     * @param userEmail - the userEmail used as username to query the db
     * @return - UserDetails used to create the User principal object
     * @throws UsernameNotFoundException - if the requested user does not exists in the db
     */
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        try {
            UserEntity userEntity = userDAO.getUserEntitiesByEmail(userEmail);
            if (userEntity == null) {
                throw new UsernameNotFoundException(userEmail);
            }
            return new StockUserPrincipal(new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>()));
        } catch (UserNotFoundException ex) {
            throw new UsernameNotFoundException("UserEntity not found");
        }
    }
}
