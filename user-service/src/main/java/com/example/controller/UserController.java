package com.example.controller;

import com.example.dto.UserDTO;
import com.example.enums.Role;
import com.example.enums.Tag;
import com.example.model.User;
import com.example.service.KeycloakService;
import com.example.service.UserService;
import lombok.AllArgsConstructor;
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
    private final PasswordEncoder passwordEncoder;
    private final KeycloakService keycloakService;

    @GetMapping("/owner")
    public String owner() {
        return "owner";
    }

    @GetMapping("/authenticated")
    public String authenticated() {
        return "authenticated";
    }

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

        if (userDTO.isEmpty())
            return "/users/UserDoesNotExist";
        model.addAttribute("user", userDTO);
        return "/users/user";
    }

    @GetMapping("/i/{userId}")
    public String getUserById(@PathVariable Long userId, Model model) {
        userService.waitASecond();
        Optional<UserDTO> userDTO = userService.findById(userId);

        if (userDTO.isEmpty())
            return "/users/UserDoesNotExist";
        model.addAttribute("user", userDTO.get());
        return "/users/user";
    }
    //endregion

    //region Registration
    @GetMapping("/registration")
    public String registration() {
        userService.waitASecond();
        return "/users/addUser";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam String displayedUsername, @RequestParam String username,
                          @RequestParam String password, @RequestParam String passwordRetry, @RequestParam String phoneNumber) {
        if (userService.findByUsername(username).isPresent())
            return "/users/loginIsBusy";
        if (!password.equals(passwordRetry))
            return "/users/passwordsDoNotMatch";
        keycloakService.createUser(username, password, "InternetShop");
        userService.save(username, displayedUsername, password, phoneNumber);
        return "redirect:/products";
    }
    //endregion

    //region DELETE
    @GetMapping("/deleteNotYourUser")
    public String deleteUser() {
        userService.waitASecond();
        return "/users/deleteNotYourUser";
    }

    @GetMapping("/deleteNotYourUserById")
    public String deleteUser(@AuthenticationPrincipal User user, Long userId, Model model, @RequestParam String password) {
        if (userService.findById(userId).isEmpty())
            return "/users/UserDoesNotExist";
        if (!passwordEncoder.matches(user.getPassword(), password))
            return "/users/passwordsDoNotMatch";
        if (user.getRole().getHierarchy() <= userService.findById(userId).get().role().getHierarchy())
            return "/users/youAreNotHigher";
        userService.deleteById(userId);
        return "redirect:/users";
    }

    @GetMapping("/deleteYourUser")
    public String deleteYourUser() {
        userService.waitASecond();
        return "/users/deleteYourUser";
    }

    @GetMapping("/deleteYourUserById")
    public String deleteYourUser(@AuthenticationPrincipal User user, @RequestParam String password) {
        userService.deleteById(user.getId());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "/users/passwordsDoNotMatch";
        }
        return "redirect:/users";
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
        if (!passwordEncoder.matches(password, user.getPassword()))
            return "/users/passwordsDoNotMatch";
        if (!newPassword.equals(retryPassword))
            return "/users/newPasswordsDoNotMatch";
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
        if (!passwordEncoder.matches(password, user.getPassword()))
            return "/users/passwordsDoNotMatch";
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
                                            @RequestParam String password) {
        if (userService.findById(userId).isEmpty())
            return "/products/productDoesNotExist";
        if (!passwordEncoder.matches(password, user.getPassword()))
            return "/users/passwordsDoNotMatch";
        userService.updateDisplayedUsernameById(userId, "User");
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
        if (userService.findById(userId).isEmpty())
            return "/users/UserDoesNotExist";
        if (user.getRole().getHierarchy() <= role.getHierarchy())
            return "/users/youAreNotHigher";
        if (role.equals(userService.findById(userId).get().role()))
            return "/users/userHasThisRole";
        if (user.getRole().getHierarchy() <= userService.findById(userId).get().role().getHierarchy())
            return "/users/youAreNotHigher";
        userService.updateRoleById(userId, role);
        return "redirect:/users/i/" + userId;
    }
    //endregion

    //region Description change
    @GetMapping("/updateYourDescription")
    public String updateDescription() {
        userService.waitASecond();
        return "/users/updateYourDescription";
    }

    @GetMapping("/updateYourDescriptionById")
    public String updateDescriptionById(@AuthenticationPrincipal User user,
                                        @RequestParam String description, @RequestParam String password) {
        if (!passwordEncoder.matches(password, user.getPassword()))
            return "/users/passwordsDoNotMatch";
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
                                               @RequestParam Long userId, @RequestParam String password) {
        if (userService.findById(userId).isEmpty())
            return "/products/productDoesNotExist";
        if (!passwordEncoder.matches(password, user.getPassword()))
            return "/users/passwordsDoNotMatch";
        userService.updateDescriptionById(userId, "Description");
        return "redirect:/users/i/" + userId;
    }

    //endregion

    //region Phone Number change
    @GetMapping("/updateYourPhoneNumber")
    public String updateYourPhoneNumber() {
        userService.waitASecond();
        return "/users/updateYourPhoneNumber";
    }

    @PostMapping("/updateYourPhoneNumberById")
    public String updateYourPhoneNumberById(@AuthenticationPrincipal User user,
                                            @RequestParam String newPhoneNumber, @RequestParam Long productId, @RequestParam String password) {
        if (userService.findById(productId).isEmpty())
            return "/products/productDoesNotExist";
        if (!passwordEncoder.matches(password, user.getPassword()))
            return "/users/passwordsDoNotMatch";
        userService.updatePhoneNumberById(productId, newPhoneNumber);
        return "redirect:/products/i/" + productId;
    }

    @GetMapping("/updateNotYourPhoneNumber")
    public String updateNotYourPhoneNumber() {
        userService.waitASecond();
        return "/users/updateNotYourDescription";
    }

    @PostMapping("/updateNotYourPhoneNumberById")
    public String updateNotYourPhoneNumberById(@AuthenticationPrincipal User user,
                                               @RequestParam String password, @RequestParam Long userId) {
        if (userService.findById(userId).isEmpty())
            return "/products/productDoesNotExist";
        if (!passwordEncoder.matches(password, user.getPassword()))
            return "/users/passwordsDoNotMatch";
        if (user.getRole().getHierarchy() <= userService.findById(userId).get().role().getHierarchy())
            return "/users/youAreNotHigher";
        userService.updatePhoneNumberById(userId, "Number has been deleted");
        return "redirect:/products/i/" + userId;
    }
    //endregion

    //region Tag change
    @GetMapping("/addTag")
    public String addTag() {
        userService.waitASecond();
        return "/users/addTag";
    }

    @PostMapping("/addTagById")
    public String addTagById(@AuthenticationPrincipal User user, @RequestParam Tag tag) {
        if (user.getTags().contains(tag))
            return "/users/tagExists";
        userService.addTag(user.getId(), tag);
        return "redirect:/users/i/" + user.getId();
    }

    @GetMapping("/removeTag")
    public String removeTag() {
        userService.waitASecond();
        return "/users/removeTag";
    }

    @PostMapping("/removeTagById")
    public String removeTagById(@AuthenticationPrincipal User user, @RequestParam Tag tag) {
        if (user.getTags().contains(tag))
            return "/users/tagDoesn'tExist";
        userService.removeTag(user.getId(), tag);
        return "redirect:/users/i/" + user.getId();
    }
    //endregion

    //endregion

}