package com.example.service;

import com.example.enums.Role;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User save(String username, String password, String email) {
        return userRepository.save(new User(username, password, email));
    }

    public void updateEmailById(Long userId, String email) {
        userRepository.updateEmailById(userId, email);
    }

    public void updatePasswordById(Long userId, String password) {
        userRepository.updatePasswordById(userId, password);
    }

    public void updateUsernameById(Long userId, String username) {
        userRepository.updateUsernameById(userId, username);
    }

    // The method is required to add a role to a user
    public void addRoleById(Long userId, Role role) {
        if (userRepository.existsById(userId)) {
            User user = findById(userId);
            if (!user.getRoles().contains(role)) {
                Set<Role> roles = user.getRoles();
                roles.add(role);
                userRepository.updateRoleById(userId, roles);
            }
        }
    }

    // The method is required to delete a role from a user
    public void deleteRoleById(Long userId, Role role) {
        if (userRepository.existsById(userId)) {
            User user = findById(userId);
            if (user.getRoles().contains(role)) {
                Set<Role> roles = user.getRoles();
                roles.remove(role);
                userRepository.updateRoleById(userId, roles);
            }
        }
    }

    // The method is required to retrieve data by the username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

}