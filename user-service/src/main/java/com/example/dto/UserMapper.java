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

}