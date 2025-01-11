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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //region Read
    @GetMapping
    public String getUsers(Model model) {
        userService.waitASecond();
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/u/{displayedUsername}")
    public String getByDisplayedUsername(@PathVariable String displayedUsername, Model model) {
        userService.waitASecond();
        model.addAttribute("users", userService.findByDisplayedUsername(displayedUsername));
        return "users";
    }

    @GetMapping("/l/{username}")
    public String getUserByLogin(@PathVariable String username, Model model) {
        userService.waitASecond();
        model.addAttribute("users", userService.findByUsername(username).stream().collect(Collectors.toList()));
        return "users";
    }

    @GetMapping("/i/{userId}")
    public String getUserById(@PathVariable Long userId, Model model) {
        userService.waitASecond();
        model.addAttribute("users", userService.findById(userId).stream().collect(Collectors.toList()));
        return "users";
    }
    //endregion

//    @GetMapping ("/register")
//    public String createUser(Model model, User user) {
//        userService.waitASecond();
//        model.addAttribute("user", new User());
//        return "addUser";
//    }
//
//    @PostMapping("/register")
//    public String createUser(@ModelAttribute User user,
//                             @RequestParam String passwordRetry,
//                             Model model) {
//        userService.waitASecond();
//        if (userService.findByUsername(user.getUsername()).isPresent()) {
//            model.addAttribute("error", "User with this username already exists.");
//            return "addUser";
//        }
//        if (!user.getPassword().equals(passwordRetry)) {
//            model.addAttribute("error", "Passwords do not match.");
//            return "addUser"; // Возвращаем форму с ошибкой
//        }
//        userService.save(user.getUsername(), user.getDisplayedUsername(), user.getPassword());
//        return "redirect:/users/i/1";
//    }

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
        userService.save(username, displayedUsername, password);
        return new ResponseEntity<>(HttpStatus.CREATED);
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
        if (userService.findById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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