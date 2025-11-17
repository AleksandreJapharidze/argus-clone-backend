package com.example.argusclone.repositories;

import com.example.argusclone.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByCourseName(String courseName);
    Optional<Course> findByCourseCode(String courseCode);
}
