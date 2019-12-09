package com.stock.app.service;

import com.stock.app.exceptions.UserAlreadyExistsException;
import com.stock.app.exceptions.UserNotFoundException;
import com.stock.app.model.User;

import java.util.List;

public interface UserManagementService {

    void checkLoginDetails(String email, String password) throws UserNotFoundException;

    void registerNewUser(User user) throws UserAlreadyExistsException;

    List<User> getAllUsers() throws Exception;

    User updateUser(User user) throws UserNotFoundException;
}
