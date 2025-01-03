package com.example.repository;

import com.example.enums.Role;
import com.example.enums.Tag;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updatePasswordById(Long id, String password);

    @Modifying
    @Query("UPDATE User u SET u.username = :username WHERE u.id = :id")
    void updateUsernameById(Long id, String username);

    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :id")
    void updateRoleById(Long id, Role role);

    @Modifying
    @Query("UPDATE User u SET u.description = :description WHERE u.id = :id")
    void updateDescriptionById(Long id, String description);

    @Modifying
    @Query("UPDATE User u SET u.updatedAt = :updatedAt WHERE u.id = :id")
    void updateUpdatedAtById(Long id, LocalDateTime updatedAt);

    @Modifying
    @Query("UPDATE User u SET u.tags = :tags WHERE u.id = :id")
    void updateTagsById(Long id, Set<Tag> tags);

}