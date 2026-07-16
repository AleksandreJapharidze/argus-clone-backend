package com.example.argusclone.repositories;

import com.example.argusclone.entities.User;
import com.example.argusclone.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.personalId = :personalId")
    Optional<User> findByPersonalId(String personalId);

    boolean existsByRoleAndRoleId(Role role, Integer roleId);
    boolean existsByPersonalId(String personalId);

}
