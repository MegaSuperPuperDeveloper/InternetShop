package com.example.service;

import com.example.enums.Role;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User save(String username, String password) {
        return userRepository.save(new User(username, password));
    }

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
    public void updateRoleById(Long userId, Role role) {
        userRepository.updateRoleById(userId, role);
    }

    @Transactional
    public void updateDescriptionById(Long id, String description) {
        userRepository.updateDescriptionById(id, description);
    }

    @Transactional
    public void updateUpdatedAtById(Long id, LocalDateTime updatedAt) {
        userRepository.updateUpdatedAtById(id, updatedAt);
    }

    // The method is required to retrieve data by the username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (findByUsername(username) == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        User user = findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    //region Checking
    public boolean isPasswordCorrect(String password) {
        return password.split("").length > 8;
    }
    //endregion

}