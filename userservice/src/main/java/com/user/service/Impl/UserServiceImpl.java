package com.user.service.Impl;

import com.user.exception.UserException;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);

    }

    @Override
    public User getUserById(Long Id) throws UserException {
        Optional<User> user = userRepository.findById(Id);
        if(user.isPresent()){
            return user.get();
        }
        throw new UserException("User not found with id " + Id);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long Id) throws UserException {

        Optional<User> existingUser = userRepository.findById(Id);

        if(existingUser.isEmpty()){
            throw new UserException("User not found with this id"+ Id);
        }
        userRepository.deleteById((long) existingUser.get().getId());

    }

    @Override
    public User updateUser(Long Id, User user) throws UserException {
        Optional<User> otp = userRepository.findById(Id);

        if(otp.isEmpty()){
            throw new UserException("User not found with this id"+ Id);
        }

        User existingUser = otp.get();

        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setPassword(user.getPassword());
        existingUser.setRole(user.getRole());

        return userRepository.save(existingUser);
    }
}
