package com.example.argusclone.repositories;

import com.example.argusclone.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByName(String name);
    Optional<Student> findByEmail(String email);
}
