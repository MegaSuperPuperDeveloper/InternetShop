package com.example.service;

import com.example.dto.UserDTO;
import com.example.dto.UserMapper;
import com.example.enums.Role;
import com.example.enums.Tag;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    //region
    public Iterable<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findById(Long userId) {
        return userRepository.findById(userId)
                .map(userMapper::mapToDTO);
    }

    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::mapToDTO);
    }

    public Iterable<UserDTO> findByDisplayedUsername(String displayedUsername) {
        return userRepository.findByDisplayedUsername(displayedUsername).stream()
                .map(userMapper::mapToDTO)
                .collect(Collectors.toList());
    }
    //endregion

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User save(String username, String displayedUsername, String password) {
        return userRepository.save(new User(displayedUsername, username.toLowerCase(), password));
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
    public void updateDisplayedUsernameById(Long userId, String username) {
        userRepository.updateDisplayedUsernameById(userId, username);
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

    //region Other functions
    public boolean isPasswordCorrect(String password) {
        return password.split("").length > 8;
    }

    public void waitASecond() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("Не удалось подождать секунду!");
        }
    }

    public Optional<User> findByUsernameForCustomUserDetailsService(String username) {
        return userRepository.findByUsername(username);
    }
    //endregion

}