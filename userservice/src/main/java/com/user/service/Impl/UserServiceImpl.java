package com.user.service.Impl;

import com.user.exception.UserException;
import com.user.model.User;
import com.user.payload.dto.KeycloakUserinfo;
import com.user.repository.UserRepository;
import com.user.service.KeycloakUserService;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    KeycloakUserService keycloakUserService;


    @Override
    public User getUserByEmail(String email) throws UserException {

        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserException("user not found with this Email"+ email);

        }

        return user;
    }

    @Override
    public User getUserFromJwtToken(String jwt) throws Exception {
        KeycloakUserinfo userinfo = keycloakUserService.fetchUserProfileByJwt(jwt);
        return userRepository.findByEmail(userinfo.getEmail());
    }

    @Override
    public User getUserById(Long id) throws UserException {

        User userId = userRepository.findById(id).orElse(null);
        if(userId == null){
            throw new UserException("user not found with this id"+id);

        }

        return userId;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
