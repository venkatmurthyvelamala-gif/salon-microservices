package com.user.service;

import com.user.exception.UserException;
import com.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    User createUser(User user);
    User getUserById(Long Id) throws UserException;
    List<User> getAllUser();
    void deleteUser(Long Id) throws UserException;
    User updateUser(Long Id, User user) throws UserException;
}
