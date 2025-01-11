package com.example.controller;

import com.example.dto.UserDTO;
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
    public Iterable<UserDTO> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/u/{displayedUsername}")
    public ResponseEntity<Iterable<UserDTO>> getByDisplayedUsername(@PathVariable String displayedUsername) {
        userService.waitASecond();
        return new ResponseEntity<>(userService.findByDisplayedUsername(displayedUsername), HttpStatus.OK);
    }

    @GetMapping("/l/{username}")
    public ResponseEntity<Optional<UserDTO>> getUserByLogin(@PathVariable String username) {
        userService.waitASecond();
        Optional<UserDTO> user = userService.findByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/i/{userId}")
    public ResponseEntity<Optional<UserDTO>> getUserById(@PathVariable Long userId) {
        userService.waitASecond();
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.findById(userId), HttpStatus.OK);
    }
    //endregion

    @PostMapping("/{displayedUsername}/{username}/{password}/{passwordRetry}")
    public ResponseEntity<User> createUser(@PathVariable String displayedUsername,
                                           @PathVariable String username,
                                           @PathVariable String password,
                                           @PathVariable String passwordRetry) {
        userService.waitASecond();
        if (userService.findByUsername(username).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (!password.equals(passwordRetry)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userService.save(username, displayedUsername, password), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User user, @PathVariable Long userId) {
        userService.waitASecond();
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user.getRole().getHierarchy() > userService.findById(userId).get().role().getHierarchy()) {
            userService.deleteById(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if (user.getId().equals(userId)) {
            userService.deleteById(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    //region UPDATE

    @PatchMapping("/{userId}/p/{currentPassword}/{newPassword}")
    public ResponseEntity<Void> updatePasswordById(@AuthenticationPrincipal User user,
                                                   @PathVariable Long userId,
                                                   @PathVariable String currentPassword,
                                                   @PathVariable String newPassword) {
        userService.waitASecond();
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!user.getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        userService.updatePasswordById(userId, currentPassword, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/u/{newUsername}")
    public ResponseEntity<Void> updateUsernameById(@AuthenticationPrincipal User user,
                                                   @PathVariable Long userId, @PathVariable String newUsername) {
        userService.waitASecond();
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user.getRole().getHierarchy() <= userService.findById(userId).get().role().getHierarchy() && !user.getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        userService.updateDisplayedUsernameById(userId, newUsername);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/l/{newLogin}")
    public ResponseEntity<Void> updateLoginById(@AuthenticationPrincipal User user,
                                                @PathVariable Long userId, @PathVariable String newLogin) {
        userService.waitASecond();
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!user.getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        userService.updateLoginById(userId, newLogin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/r/{role}")
    public ResponseEntity<Void> updateRoleById(@AuthenticationPrincipal User user,
                                               @PathVariable Long userId, @PathVariable Role role) {
        userService.waitASecond();
//        if (userService.findById(userId).isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        if (user.getRole().getHierarchy() <= role.getHierarchy()) {
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//        if (role == userService.findById(userId).get().role()) {
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//        if (user.getRole().getHierarchy() <= userService.findById(userId).get().role().getHierarchy()) {
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
        userService.updateRoleById(userId, role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/d/{description}")
    public ResponseEntity<Void> updateDescriptionById(@AuthenticationPrincipal User user,
                                                      @PathVariable Long userId,  @PathVariable String description) {
        userService.waitASecond();
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user.getRole().getHierarchy() <= userService.findById(userId).get().role().getHierarchy() && !user.getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        userService.updateDescriptionById(userId, description);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Решить проблему с тегами
    @PatchMapping("/{userId}/add/{tag}")
    public ResponseEntity<Void> addTagById(@AuthenticationPrincipal User user,
                                           @PathVariable Long userId, @PathVariable Tag tag) {
        userService.waitASecond();
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!user.getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        userService.addTagToUser(userId, tag);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/remove/{tag}")
    public ResponseEntity<Void> removeTagById(@AuthenticationPrincipal User user,
                                              @PathVariable Long userId, @PathVariable Tag tag) {
        userService.waitASecond();
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!user.getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        userService.removeTagToUser(userId, tag);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //endregion
    //endregion

}