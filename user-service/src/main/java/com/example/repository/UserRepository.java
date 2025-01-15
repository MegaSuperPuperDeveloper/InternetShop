package com.example.repository;

import com.example.enums.Role;
import com.example.enums.Tag;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByDisplayedUsername(String displayedUsername);

    Optional<User> findByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updatePasswordById(Long id, String password);

    @Modifying
    @Query("UPDATE User u SET u.username = :username WHERE u.id = :id")
    void updateLoginById(Long id, String username);

    @Modifying
    @Query("UPDATE User u SET u.displayedUsername = :displayedUsername WHERE u.id = :id")
    void updateDisplayedUsernameById(Long id, String displayedUsername);

    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :id")
    void updateRoleById(Long id, Role role);

    @Modifying
    @Query("UPDATE Product p SET p.authorPhoneNumber = :authorPhoneNumber WHERE p.id = :id")
    void updatePhoneNumberById(Long id, String authorPhoneNumber);

    @Modifying
    @Query("UPDATE User u SET u.description = :description WHERE u.id = :id")
    void updateDescriptionById(Long id, String description);

}