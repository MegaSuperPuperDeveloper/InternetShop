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
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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
        User user = new User(username, displayedUsername, passwordEncoder.encode(password), phoneNumber);
        userService.save(user);
        keycloakService.createUser(user, password, "master");
        return "redirect:/users/i/" + user.getId();
    }
    //endregion

    //region DELETE
    @GetMapping("/deleteNotYourUser")
    public String deleteUser() {
        userService.waitASecond();
        return "/users/deleteNotYourUser";
    }

    @GetMapping("/deleteNotYourUserById")
    public String deleteUser(@AuthenticationPrincipal OidcUser oidcUser, Long userId, @RequestParam String password) {
        String keycloakUsersId = oidcUser.getSubject();
        String keycloakIdWhoNeedDelete = userService.findUserById(userId).get().getKeycloakId();

        User user = userService.findByKeycloakId(keycloakUsersId).get();

        if (!passwordEncoder.matches(password, user.getPassword()))
            return "/users/passwordsDoNotMatch";
        if (user.getRole().getHierarchy() <= userService.findById(userId).get().role().getHierarchy())
            return "/users/youAreNotHigher";

        keycloakService.deleteUserByKeycloakId(keycloakIdWhoNeedDelete, "master");
        userService.deleteById(userId);
        return "redirect:/users";
    }

    @GetMapping("/deleteYourUser")
    public String deleteYourUser() {
        userService.waitASecond();
        return "/users/deleteYourUser";
    }

    @GetMapping("/deleteYourUserById")
    public String deleteYourUser(@RequestParam String password, @AuthenticationPrincipal OidcUser oidcUser) {
        String keycloakId = oidcUser.getSubject();

        User user = userService.findByKeycloakId(keycloakId).get();

        if (!passwordEncoder.matches(password, user.getPassword()))
            return "/users/passwordsDoNotMatch";

        keycloakService.deleteUserByKeycloakId(keycloakId, "master");
        userService.deleteById(user.getId());
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
    public String updatePasswordById(@AuthenticationPrincipal OidcUser oidcUser, @RequestParam String password,
                                     @RequestParam String newPassword, @RequestParam String retryPassword) {
        String keycloakId = oidcUser.getSubject();

        Optional<User> userOptional = userService.findByKeycloakId(keycloakId);

        User user = userOptional.get();

        if (!passwordEncoder.matches(password, user.getPassword()))
            return "/users/passwordsDoNotMatch";
        if (!newPassword.equals(retryPassword))
            return "/users/newPasswordsDoNotMatch";

        keycloakService.updatePassword(keycloakId, newPassword, "master");
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
    public String updateLoginById(@AuthenticationPrincipal OidcUser oidcUser,
                                  @RequestParam String username, @RequestParam String password) {
        String keycloakId = oidcUser.getSubject();

        Optional<User> userOptional = userService.findByKeycloakId(keycloakId);

        User user = userOptional.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "/users/passwordsDoNotMatch";
        }
        if (userService.findByUsername(username).isPresent()) {
            return "/users/loginIsBusy";
        }
        userService.updateLoginById(user.getId(), username);
        keycloakService.updateUsername(keycloakId, username,"master");
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
    public String updateUsernameById(@AuthenticationPrincipal OidcUser oidcUser,
                                     @RequestParam String password, @RequestParam String displayedUsername) {
        String keycloakId = oidcUser.getSubject();

        Optional<User> userOptional = userService.findByKeycloakId(keycloakId);

        User user = userOptional.get();

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
    public String updateNotYourUsernameById(@AuthenticationPrincipal OidcUser oidcUser, @RequestParam Long userId,
                                            @RequestParam String password) {
        String keycloakId = oidcUser.getSubject();

        Optional<User> userOptional = userService.findByKeycloakId(keycloakId);

        User user = userOptional.get();

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
    public String updateRoleById(@AuthenticationPrincipal OidcUser oidcUser, @RequestParam Role role, @RequestParam Long userId) {
        String keycloakId = oidcUser.getSubject();

        Optional<User> userOptional = userService.findByKeycloakId(keycloakId);

        User user = userOptional.get();

        if (userService.findById(userId).isEmpty())
            return "/users/UserDoesNotExist";
        if (user.getRole().getHierarchy() <= role.getHierarchy())
            return "/users/youAreNotHigher";
        if (role.equals(userService.findById(userId).get().role()))
            return "/users/userHasThisRole";
        if (user.getRole().getHierarchy() <= userService.findById(userId).get().role().getHierarchy())
            return "/users/youAreNotHigher";

        userService.updateRoleById(userId, role);
        keycloakService.updateRole(keycloakId, role.getSystemName(),"master");
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
    public String updateDescriptionById(@AuthenticationPrincipal OidcUser oidcUser,
                                        @RequestParam String description, @RequestParam String password) {
        String keycloakId = oidcUser.getSubject();

        User user = userService.findByKeycloakId(keycloakId).get();

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
    public String updateNotYourDescriptionById(@AuthenticationPrincipal OidcUser oidcUser,
                                               @RequestParam Long userId, @RequestParam String password) {
        String keycloakId = oidcUser.getSubject();

        Optional<User> userOptional = userService.findByKeycloakId(keycloakId);

        User user = userOptional.get();

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
    public String updateYourPhoneNumberById(@AuthenticationPrincipal OidcUser oidcUser,
                                            @RequestParam String newPhoneNumber, @RequestParam Long productId, @RequestParam String password) {
        String keycloakId = oidcUser.getSubject();

        Optional<User> userOptional = userService.findByKeycloakId(keycloakId);

        User user = userOptional.get();

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
    public String updateNotYourPhoneNumberById(@AuthenticationPrincipal OidcUser oidcUser,
                                               @RequestParam String password, @RequestParam Long userId) {
        String keycloakId = oidcUser.getSubject();

        Optional<User> userOptional = userService.findByKeycloakId(keycloakId);

        User user = userOptional.get();

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
    public String addTagById(@AuthenticationPrincipal OidcUser oidcUser, @RequestParam Tag tag) {
        String keycloakId = oidcUser.getSubject();

        Optional<User> userOptional = userService.findByKeycloakId(keycloakId);

        User user = userOptional.get();

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
    public String removeTagById(@AuthenticationPrincipal OidcUser oidcUser, @RequestParam Tag tag) {
        String keycloakId = oidcUser.getSubject();

        Optional<User> userOptional = userService.findByKeycloakId(keycloakId);

        User user = userOptional.get();

        if (user.getTags().contains(tag))
            return "/users/tagDoesn'tExist";
        userService.removeTag(user.getId(), tag);
        return "redirect:/users/i/" + user.getId();
    }
    //endregion

}