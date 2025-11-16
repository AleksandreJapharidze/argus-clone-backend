package com.example.argusclone.repositories;

import com.example.argusclone.entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
    Optional<Instructor> findByName(String name);
    Optional<Instructor> findByEmail(String email);
}
