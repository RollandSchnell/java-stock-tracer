package com.stock.app.controller;

import com.stock.app.exceptions.UserAlreadyExistsException;
import com.stock.app.exceptions.UserNotFoundException;
import com.stock.app.model.User;
import com.stock.app.service.UserManagementService;
import com.stock.app.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/userApi")
public class UserController {

    private final static Logger Log = LoggerFactory.getLogger(UserController.class);

    private UserManagementService userManagementService;

    @Autowired
    public UserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity registerUser(@RequestBody User user) {

        Log.info("[{}] User registration with {}", DateUtils.getTimestamp(), user.toString());
        try {
            userManagementService.registerNewUser(user);
            return new ResponseEntity<>("User created", HttpStatus.OK);
        } catch(UserAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity updateUser(@RequestBody User user) {

        Log.info("[{}] User update with {}", DateUtils.getTimestamp(), user.toString());
        try {
            userManagementService.updateUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch(UserNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/loginUser", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity loginUser(@RequestBody User user) {

        Log.info("[{}] User login with {} | {}", DateUtils.getTimestamp(), user.getEmail(), user.getPassword());
        try {
            userManagementService.checkLoginDetails(user.getEmail(), user.getPassword());
            return new ResponseEntity<>("User logged in", HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getAllUsers() {

        Log.info("[{}] Get all users", DateUtils.getTimestamp());

        List<User> users;
        try {
            users = userManagementService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
