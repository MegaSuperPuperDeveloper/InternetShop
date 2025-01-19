package com.example.dto;

import com.example.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserMapper {

    public UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getDisplayedUsername(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getTags(),
                user.getCreatedAt(),
                user.getDescription()
        );
    }

}