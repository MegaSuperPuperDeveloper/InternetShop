package com.example.controller;

import com.example.enums.Role;
import com.example.model.User;
import com.example.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    //region Read
    @GetMapping("/users")
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

    @GetMapping("/profile/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        if (userService.findByUsername(username) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }
    //endregion

    @PostMapping("/{username}/{password}")
    public ResponseEntity<User> createUser(@PathVariable String username,
                                           @PathVariable String password) {
        return new ResponseEntity<>(userService.save(username, password), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        if (userService.findById(userId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //region UPDATE

    @PatchMapping("/{userId}/{password}/p/{newPassword}")
    public ResponseEntity<Void> updatePasswordById(@PathVariable Long userId,
                                                   @PathVariable String password,
                                                   @PathVariable String newPassword) {
        if (userService.findById(userId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.updatePasswordById(userId, password, newPassword);
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/u/{newUsername}")
    public ResponseEntity<Void> updateUsernameById(@PathVariable Long userId,
                                                   @PathVariable String newUsername) {
        if (userService.findById(userId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.updateUsernameById(userId, newUsername);
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/r/{role}")
    public ResponseEntity<Void> updateRoleById(@PathVariable Long userId,
                                                   @PathVariable Role role) {
        if (userService.findById(userId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.updateRoleById(userId, role);
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/d{description}")
    public ResponseEntity<Void> updateDescriptionById(@PathVariable Long userId,
                                                      @PathVariable String description) {
        if (userService.findById(userId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.updateDescriptionById(userId, description);
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //endregion

}