package com.example.dto;

import com.example.model.User;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class UserMapper {

    public UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getDisplayedUsername(),
                user.getRole(),
                user.getTags(),
                user.getCreatedAt(),
                user.getDescription()
        );
    }

    public User mapToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.id());
        user.setDisplayedUsername(userDTO.displayedUsername());
        user.setRole(userDTO.role());
        user.setTags(userDTO.tags());
        user.setCreatedAt(userDTO.createdAt());
        user.setDescription(userDTO.description());
        return user;
    }

}