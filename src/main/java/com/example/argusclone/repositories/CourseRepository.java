package com.example.argusclone.repositories;

import com.example.argusclone.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByName(String name);
    Optional<Course> findByCourseCode(String courseCode);
}
