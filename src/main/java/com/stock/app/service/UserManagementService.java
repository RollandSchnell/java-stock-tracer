package com.stock.app.service;

import com.stock.app.exceptions.UserAlreadyExistsException;
import com.stock.app.exceptions.UserNotFoundException;
import com.stock.app.model.UserEntity;

import java.util.List;

public interface UserManagementService {

    String authenticateUser(String email, String password) throws UserNotFoundException;

    UserEntity registerNewUser(UserEntity userEntity) throws UserAlreadyExistsException;

    List<UserEntity> getAllUsers() throws Exception;

    UserEntity updateUser(UserEntity userEntity) throws UserNotFoundException;
}
