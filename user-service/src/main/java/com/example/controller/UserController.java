package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Iterable<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        if (userService.findById(userId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.findById(userId), HttpStatus.OK);
    }

    @GetMapping("/profiles/{username}")
    public ResponseEntity<Optional<User>> getUserByUsername(@PathVariable String username) {
        if (userService.findByUsername(username).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    @PostMapping("/{username}/{password}/{email}")
    public ResponseEntity<User> createUser(@PathVariable String username,
                                           @PathVariable String password,
                                           @PathVariable String email) {
        return new ResponseEntity<>(userService.save(username, password, email), HttpStatus.CREATED);
    }

//    @DeleteMapping("/delete/profile/{userId}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
//        if (userService.findById(userId) == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        userService.deleteById(userId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    // Доделать обновление данных

}