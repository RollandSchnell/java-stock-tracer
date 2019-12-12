package com.stock.app.service;

import com.stock.app.dao.UserDAO;
import com.stock.app.exceptions.UserAlreadyExistsException;
import com.stock.app.exceptions.UserNotFoundException;
import com.stock.app.model.UserEntity;
import com.stock.app.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for user resource management.
 *
 * @author rolland.schnell
 */
@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final static Logger Log = LoggerFactory.getLogger(UserManagementServiceImpl.class);

    private UserDAO userDAO;

    private AuthenticationManager authenticationManager;

    private StockUserDetailsService stockUserDetailsService;

    private JwtUtils jwtUtils;

    @Autowired
    public UserManagementServiceImpl(UserDAO userDAO, AuthenticationManager authenticationManager,
                                     StockUserDetailsService stockUserDetailsService, JwtUtils jwtUtils) {
        this.userDAO = userDAO;
        this.authenticationManager = authenticationManager;
        this.stockUserDetailsService = stockUserDetailsService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Check login credentials for a specific user, if the user credentials match the ones from the db a response with
     * status 200 is returned
     * @param email - email for the user to be logged in
     * @param password - password for the user to be logged in in hashed format
     * @return - the jwt resource access token
     * @throws UserNotFoundException - if the user does not exists in the db
     */
    @Override
    public String authenticateUser(String email, String password) throws UserNotFoundException {
       try {
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
       } catch (BadCredentialsException ex) {
           throw new UserNotFoundException("Invalid login credentials");
       } catch (Exception e) {
           e.printStackTrace();
       }

       final UserDetails userDetails = stockUserDetailsService.loadUserByUsername(email);

       return jwtUtils.generateToken(userDetails);
    }

    /**
     * Save a new userEntity in the db if not exists.
     * @param userEntity - the new userEntity details
     * @return - the saved userEntity object
     * @throws UserAlreadyExistsException - if the userEntity already exists with the same email
     */
    @Override
    public UserEntity registerNewUser(UserEntity userEntity) throws UserAlreadyExistsException {
        try {

            UserEntity userEntityFromDb = userDAO.getUserEntitiesByEmail(userEntity.getEmail());

            if (userEntityFromDb == null) {
                userDAO.save(userEntity);
                return userEntity;
            } else {
                throw new UserAlreadyExistsException("UserEntity already exists");
            }
        } catch (Exception e) {
            throw new UserAlreadyExistsException("UserEntity already exists");
        }
    }

    /**
     * Returns all users from db (for administration purposes)
     * @return - list of users
     * @throws Exception - if the users could not be queried from the db
     */
    @Override
    public List<UserEntity> getAllUsers() throws Exception {
        try {
            return userDAO.findAll();
        } catch(Exception e) {
            throw e;
        }
    }

    /**
     * If the userEntity exists update it with new userEntity details. (Could be used for userEntity registration also, if we remove the exception part)
     * @param userEntity - new userEntity details
     * @return - the updated userEntity
     * @throws UserNotFoundException - if the userEntity does not exists we dont have what to update
     */
    @Override
    public UserEntity updateUser(UserEntity userEntity) throws UserNotFoundException {
        try {

            UserEntity userEntityFromDb = userDAO.getUserEntitiesByEmail(userEntity.getEmail());

            if (userEntityFromDb == null) {
                throw new UserNotFoundException("UserEntity not found");
            } else {
                userEntityFromDb.setEmail(userEntity.getEmail());
                userEntityFromDb.setFirstName(userEntity.getFirstName());
                userEntityFromDb.setLastName((userEntity.getLastName()));
                userEntityFromDb.setPassword(userEntity.getPassword());
                userDAO.save(userEntityFromDb);

                return userEntityFromDb;
            }
        } catch (Exception e) {
            throw new UserNotFoundException("UserEntity not found");
        }
    }

}
