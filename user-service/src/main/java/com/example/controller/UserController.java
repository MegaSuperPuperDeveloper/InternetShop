package com.example.controller;

import com.example.enums.Role;
import com.example.enums.Tag;
import com.example.model.User;
import com.example.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //region Read
    @GetMapping
    public Iterable<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/u/{displayedUsername}")
    public ResponseEntity<Iterable<User>> getByDisplayedUsername(@PathVariable String displayedUsername) {
        return new ResponseEntity<>(userService.findByDisplayedUsername(displayedUsername), HttpStatus.OK);
    }

    @GetMapping("/l/{username}")
    public ResponseEntity<Optional<User>> getUserByLogin(@PathVariable String username) {
        if (userService.findByUsername(username).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<User> user = userService.findByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/i/{userId}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long userId) {
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.findById(userId), HttpStatus.OK);
    }
    //endregion

    @PostMapping("/{displayedUsername}/{username}/{password}")
    public ResponseEntity<User> createUser(@PathVariable String displayedUsername,
                                           @PathVariable String username,
                                           @PathVariable String password) {
        if (userService.findByUsername(username).isEmpty()) {
            return new ResponseEntity<>(userService.save(username, displayedUsername, password), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    //region Сделать контроллеры с использованием Authentication authentication
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId, @AuthenticationPrincipal User user) {
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        if (!user.getRole().getHierarchy() > userService.findById(userId).getRole().getHierarchy()) {
//            userService.deleteById(userId);
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //region UPDATE

    @PatchMapping("/{userId}/p/{currentPassword}/{newPassword}")
    public ResponseEntity<Void> updatePasswordById(@PathVariable Long userId,
                                                   @PathVariable String currentPassword,
                                                   @PathVariable String newPassword) {
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.updatePasswordById(userId, currentPassword, newPassword);
        userService.updateUpdatedAtById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/u/{newUsername}")
    public ResponseEntity<Void> updateUsernameById(@PathVariable Long userId,
                                                   @PathVariable String newUsername) {
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.updateUsernameById(userId, newUsername);
        userService.updateUpdatedAtById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/u/{newLogin}")
    public ResponseEntity<Void> updateLoginById(@PathVariable Long userId,
                                                @PathVariable String newLogin) {
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.updateLoginById(userId, newLogin);
        userService.updateUpdatedAtById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/r/{role}")
    public ResponseEntity<Void> updateRoleById(@PathVariable Long userId,
                                                   @PathVariable Role role) {
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.updateRoleById(userId, role);
        userService.updateUpdatedAtById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/d{description}")
    public ResponseEntity<Void> updateDescriptionById(@PathVariable Long userId,
                                                      @PathVariable String description) {
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.updateDescriptionById(userId, description);
        userService.updateUpdatedAtById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/add/{tag}")
    public ResponseEntity<Void> addTagById(@PathVariable Long userId, @PathVariable Tag tag) {
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.addTagToUser(userId, tag);
        userService.updateUpdatedAtById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/remove/{tag}")
    public ResponseEntity<Void> removeTagById(@PathVariable Long userId, @PathVariable Tag tag) {
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.removeTagToUser(userId, tag);
        userService.updateUpdatedAtById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //endregion
    //endregion

}