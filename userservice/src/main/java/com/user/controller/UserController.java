package com.user.controller;

import com.user.exception.UserException;
import com.user.model.User;
import com.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user){
        User saveUser = userService.createUser(user);
        return new ResponseEntity<>(saveUser, HttpStatus.CREATED);
    }

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws UserException {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user,
                                           @PathVariable Long id) throws UserException {
        User existingUser = userService.updateUser(id, user);
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }

    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long Id) throws UserException {
        userService.deleteUser(Id);
        return new ResponseEntity<>("user deleted Successfully",HttpStatus.ACCEPTED);

    }
}
