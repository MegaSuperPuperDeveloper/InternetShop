package com.example.service;

import com.example.enums.Role;
import com.example.enums.Tag;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService{

    private final UserRepository userRepository;

    //region READ
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Iterable<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
    //endregion

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User save(String username, String login, String password) {
        if (!isLoginBusy(login)) {
            return userRepository.save(new User(username, login, password));
        }
        return null;
    }

    //region UPDATE

    @Transactional
    public void updatePasswordById(Long userId, String password, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user.getPassword().equals(password) && isPasswordCorrect(newPassword)) {
            userRepository.updatePasswordById(userId, newPassword);
        }
    }

    @Transactional
    public void updateUsernameById(Long userId, String username) {
        userRepository.updateUsernameById(userId, username);
    }

    @Transactional
    public void updateLoginById(Long userId, String login) {
        userRepository.updateLoginById(userId, login);
    }

    @Transactional
    public void updateRoleById(Long userId, Role role) {
        userRepository.updateRoleById(userId, role);
    }

    @Transactional
    public void updateDescriptionById(Long id, String description) {
        userRepository.updateDescriptionById(id, description);
    }

    @Transactional
    public void updateUpdatedAtById(Long id) {
        userRepository.updateUpdatedAtById(id, LocalDateTime.now());
    }

    @Transactional
    public void addTagToUser(Long userId, Tag tag) {
        if (!userRepository.findById(userId).get().getTags().contains(tag)) {
            Set<Tag> tags = userRepository.findById(userId).get().getTags();
            tags.add(tag);
            userRepository.updateTagsById(userId, tags);
        }
    }

    @Transactional
    public void removeTagToUser(Long userId, Tag tag) {
        if (userRepository.findById(userId).get().getTags().contains(tag)) {
            Set<Tag> tags = userRepository.findById(userId).get().getTags();
            tags.remove(tag);
            userRepository.updateTagsById(userId, tags);
        }
    }

    //endregion

    //region Checking
    public boolean isPasswordCorrect(String password) {
        return password.split("").length > 8;
    }

    public boolean isLoginBusy(String login) {
        return userRepository.findByLogin(login).isPresent();
    }
    //endregion

}