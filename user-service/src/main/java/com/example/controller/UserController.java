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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        return "/users/users";
    }

    @GetMapping("/u/{displayedUsername}")
    public String getByDisplayedUsername(@PathVariable String displayedUsername, Model model) {
        userService.waitASecond();
        model.addAttribute("users", userService.findByDisplayedUsername(displayedUsername));
        return "/users/users";
    }

    @GetMapping("/l/{username}")
    public String getUserByLogin(@PathVariable String username, Model model) {
        userService.waitASecond();
        Optional<UserDTO> userDTO = userService.findByUsername(username);

        if (userDTO.isEmpty()) {
            return "/users/UserDoesNotExist";
        }
        model.addAttribute("user", userDTO);
        return "/users/user";
    }

    @GetMapping("/i/{userId}")
    public String getUserById(@PathVariable Long userId, Model model) {
        userService.waitASecond();
        Optional<UserDTO> userDTO = userService.findById(userId);

        if (userDTO.isEmpty()) {
            return "/users/UserDoesNotExist";
        }
        model.addAttribute("user", userDTO.get());
        return "/users/user";
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
        return "/users/addUser";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return "/users/loginIsBusy";
        }
        if (!user.getPassword().equals(model.getAttribute("passwordRetry"))) {
            return "/users/passwordsDoNotMatch";
        }
        if (isPasswordCorrect(model.getAttribute("password").toString())) {
            return "/users/PasswordIsLesserThenEight";
        }
        userService.save(user.getDisplayedUsername(), user.getUsername(), user.getPassword());
        return getUsers(model);
    }
    //endregion

    //region DELETE(Сделать удаление самого себя)
    @GetMapping("/deleteUser")
    public String deleteUser() {
        userService.waitASecond();
        return "/users/deleteUser";
    }

    @GetMapping("/deleteUserById")
    public String deleteUser(@AuthenticationPrincipal User user, Long userId, Model model) {
        if (userService.findById(userId).isEmpty()) {
            return "/users/UserDoesNotExist";
        }
        if (user.getRole().getHierarchy() <= userService.findById(userId).get().role().getHierarchy()) {
            return "/users/youAreNotHigher";
        }
        userService.deleteById(userId);
        return getUsers(model);
    }
    //endregion

    //region Password Change
    @GetMapping("/updatePassword")
    public String updatePassword() {
        userService.waitASecond();
        return "/users/updatePassword";
    }

    @GetMapping("/updatePasswordById")
    public String updatePasswordById(@AuthenticationPrincipal User user, @RequestParam String password,
                                     @RequestParam String newPassword, @RequestParam String retryPassword) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "/users/passwordsDoNotMatch";
        }
        if (!newPassword.equals(retryPassword)) {
            return "/users/newPasswordsDoNotMatch";
        }
        userService.updatePasswordById(user.getId(), newPassword);
        return "redirect:/users/i/" + user.getId();
    }
    //endregion

    //region Login Change
    @GetMapping("/updateLogin")
    public String updateLogin() {
        userService.waitASecond();
        return "/users/updateLogin";
    }

    @GetMapping("/updateLoginById")
    public String updateLoginById(@AuthenticationPrincipal User user,
                                  @RequestParam String username, @RequestParam String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "/users/passwordsDoNotMatch";
        }
        userService.updateLoginById(user.getId(), username);
        return "redirect:/users/i/" + user.getId();
    }
    //endregion

    //region Username change
    @GetMapping("/updateYourUsername")
    public String updateYourUsername() {
        userService.waitASecond();
        return "/users/updateUsernameForYourself";
    }

    @GetMapping("/updateYourUsernameById")
    public String updateUsernameById(@AuthenticationPrincipal User user,
                                     @RequestParam String password, @RequestParam String displayedUsername) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "/users/passwordsDoNotMatch";
        }
        userService.updateDisplayedUsernameById(user.getId(), displayedUsername);
        return "redirect:/users/i/" + user.getId();
    }

    @GetMapping("/updateNotYourUsername")
    public String updateNotYourUsername() {
        userService.waitASecond();
        return "/users/updateUsernameForOtherPerson";
    }

    @GetMapping("/updateNotYourUsernameById")
    public String updateNotYourUsernameById(@AuthenticationPrincipal User user, @RequestParam Long userId,
                                            @RequestParam String password, @RequestParam String displayedUsername) {
        if (userService.findById(userId).isEmpty()) {
            return "/users/UserDoesNotExist";
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "/users/passwordsDoNotMatch";
        }
        if (user.getRole().getHierarchy() <= userService.findById(userId).get().role().getHierarchy()) {
            return "/users/youAreNotHigher";
        }
        userService.updateDisplayedUsernameById(userId, displayedUsername);
        return "redirect:/users/i/" + userId;
    }
    //endregion

    //region Role Change
    @GetMapping("/updateRole")
    public String updateRole() {
        userService.waitASecond();
        return "/users/updateRole";
    }

    @GetMapping("/updateRoleById")
    public String updateRoleById(@AuthenticationPrincipal User user, @RequestParam Role role, @RequestParam Long userId) {
        if (userService.findById(userId).isEmpty()) {
            return "/users/UserDoesNotExist";
        }
        if (user.getRole().getHierarchy() <= role.getHierarchy()) {
            return "/users/youAreNotHigher";
        }
        if (role.equals(userService.findById(userId).get().role())) {
            return "/users/userHasThisRole";
        }
        if (user.getRole().getHierarchy() <= userService.findById(userId).get().role().getHierarchy()) {
            return "/users/youAreNotHigher";
        }
        userService.updateRoleById(userId, role);
        return "redirect:/users/i/" + userId;
    }
    //endregion

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


    //region Description change
    @GetMapping("/updateYourDescription")
    public String updateDescription() {
        userService.waitASecond();
        return "/users/updateYourDescription";
    }

    @GetMapping("/updateYourDescriptionById")
    public String updateDescriptionById(@AuthenticationPrincipal User user,
                                        @RequestParam String description) {
        userService.updateDescriptionById(user.getId(), description);
        return "redirect:/users/i/" + user.getId();
    }

    @GetMapping("updateNotYourDescription")
    public String updateNotYourDescription() {
        userService.waitASecond();
        return "/users/updateNotYourDescription";
    }

    @GetMapping("updateNotYourDescriptionById")
    public String updateNotYourDescriptionById(@AuthenticationPrincipal User user,
                                               @RequestParam String description, @RequestParam Long userId) {
        if (userService.findById(userId).isEmpty()) {
            return "/users/UserDoesNotExist";
        }
        if (user.getRole().getHierarchy() <= userService.findById(userId).get().role().getHierarchy()) {
            return "/users/youAreNotHigher";
        }
        userService.updateDescriptionById(userId, description);
        return "redirect:/users/i/" + userId;
    }

    //endregion

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