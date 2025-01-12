package com.example.controller;

import com.example.enums.Role;
import com.example.enums.Tag;
import com.example.model.User;
import com.example.service.ProductService;
import com.example.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private PasswordEncoder passwordEncoder;

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

    @PostMapping("/{displayedUsername}/{username}/{password}/{passwordRetry}")
    public ResponseEntity<User> createUser(@PathVariable String displayedUsername,
                                           @PathVariable String username,
                                           @PathVariable String password,
                                           @PathVariable String passwordRetry) {
        if (userService.findByUsername(username).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (!password.equals(passwordRetry)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        userService.waitASecond();
        userService.save(username, displayedUsername, password);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //region Registration
    @GetMapping("/registration")
    public String registration() {
        userService.waitASecond();
        return "addUser";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return "loginIsBusy";
        }
        if (!user.getPassword().equals(model.getAttribute("passwordRetry"))) {
            return "passwordsDoNotMatch";
        }
        if (isPasswordCorrect(model.getAttribute("password").toString())) {
            return "PasswordIsLesserThenEight";
        }
        userService.save(user.getDisplayedUsername(), user.getUsername(), user.getPassword());
        return getUsers(model);
    }
    //endregion

    //region DELETE
    @GetMapping("/deleteUser")
    public String deleteUser() {
        userService.waitASecond();
        return "deleteUser";
    }

    @GetMapping("/deleteUserById")
    public String deleteUser(@AuthenticationPrincipal User user, Long userId, Model model) {
        if (userService.findById(userId).isEmpty()) {
            return "UserDoesNotExist";
        }
        if (user.getRole().getHierarchy() <= userService.findById(userId).get().role().getHierarchy()) {
            return "youAreNotHigher";
        }
        userService.deleteById(userId);
        return getUsers(model);
    }
    //endregion

    //region Password Change
    @GetMapping("/updatePassword")
    public String updatePassword() {
        userService.waitASecond();
        return "updatePassword";
    }

    @GetMapping("/updatePasswordById")
    public String updatePasswordById(@AuthenticationPrincipal User user, @RequestParam String password,
                                     @RequestParam String newPassword, @RequestParam String retryPassword) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "passwordsDoNotMatch";
        }
        if (!newPassword.equals(retryPassword)) {
            return "newPasswordsDoNotMatch";
        }
        userService.updatePasswordById(user.getId(), newPassword);
        return "redirect:/users/i/" + user.getId();
    }
    //endregion

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

    //region Решить проблему с тегами
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

    //region Other Functions
    public boolean isPasswordCorrect(String password) {
        return password.split("").length > 8;
    }
    //endregion

}