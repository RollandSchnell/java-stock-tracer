package com.stock.app.controller;

import com.stock.app.exceptions.UserAlreadyExistsException;
import com.stock.app.exceptions.UserNotFoundException;
import com.stock.app.model.UserEntity;
import com.stock.app.service.UserManagementService;
import com.stock.app.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Main controller for user API related REST calls used for user management.
 *
 * @author rolland.schnell
 */
@RestController
@RequestMapping("/userApi")
public class UserController {

    private final static Logger Log = LoggerFactory.getLogger(UserController.class);

    private UserManagementService userManagementService;

    @Autowired
    public UserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    /**
     * Create a new userEntity identified by unique email if not exists, else return UserAlreadyExists exception.
     * @param userEntity - the userEntity details to be saved
     * @return - returns the saved userEntity if OK else error message.
     */
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity registerUser(@RequestBody UserEntity userEntity) {

        Log.info("[{}] UserEntity registration with {}", DateUtils.getTimestamp(), userEntity.toString());

        UserEntity savedUserEntity;

        try {
            savedUserEntity = userManagementService.registerNewUser(userEntity);
            return new ResponseEntity<>(savedUserEntity, HttpStatus.OK);
        } catch(UserAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Updates and existing userEntity if exists.
     * @param userEntity - the new userEntity details
     * @return - the updated userEntity if exists else error message.
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.PUT)
    public @ResponseBody
    ResponseEntity updateUser(@RequestBody UserEntity userEntity) {

        Log.info("[{}] UserEntity update with {}", DateUtils.getTimestamp(), userEntity.toString());

        UserEntity updatedUserEntity;

        try {
            updatedUserEntity = userManagementService.updateUser(userEntity);
            return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
        } catch(UserNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Verifies login credentials from db and logs in the userEntity if credentials match else return an error message.
     * @param userEntity - userEntity details containing username and password in SHA-256 hash format.
     * @return login success message(in future access token) else error message.
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity loginUser(@RequestBody UserEntity userEntity) {

        Log.info("[{}] UserEntity login with {} | {}", DateUtils.getTimestamp(), userEntity.getEmail(), userEntity.getPassword());

        String accessToken;

        try {
            accessToken = userManagementService.authenticateUser(userEntity.getEmail(), userEntity.getPassword());
            return new ResponseEntity<>(accessToken, HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Returns all users registered in the db.
     * @return UserEntity object list if all OK else error message.
     */
    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getAllUsers() {

        Log.info("[{}] Get all userEntities", DateUtils.getTimestamp());

        List<UserEntity> userEntities;

        try {
            userEntities = userManagementService.getAllUsers();
            return new ResponseEntity<>(userEntities, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
