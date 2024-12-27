package com.example.repository;

import com.example.enums.Role;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("UPDATE User u SET u.roles = :roles WHERE u.id = :id")
    void updateRoleById(Long id, Set<Role> roles);

    User findByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updatePasswordById(Long id, String password);

    @Modifying
    @Query("UPDATE User u SET u.username = :username WHERE u.id = :id")
    void updateUsernameById(Long id, String username);

}