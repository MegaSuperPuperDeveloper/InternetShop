package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SystemController {

    private final UserService userService;

    @GetMapping("/notDto")
    public Iterable<User> owner() {
        return userService.findAllUsers();
    }
}
