package com.example.dto;

import com.example.enums.Role;
import com.example.enums.Tag;
import java.util.Set;

public record UserDTO(Long id, String displayedUsername, Role role, Set<Tag> tags, String createdAt, String description) {}