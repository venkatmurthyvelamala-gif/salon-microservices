package com.user.service;

import com.user.exception.UserException;
import com.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    User getUserByEmail(String email) throws UserException;
    User getUserFromJwtToken(String jwt) throws Exception;
    User getUserById(Long id) throws UserException;
    List<User> getAllUsers();


}
