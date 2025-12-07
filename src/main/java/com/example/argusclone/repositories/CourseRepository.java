package com.example.argusclone.repositories;

import com.example.argusclone.entities.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Override
    @EntityGraph(attributePaths = {"syllabus", "instructors"})
    Optional<Course> findById(Integer id);

    Optional<Course> findByCourseName(String courseName);
    Optional<Course> findByCourseCode(String courseCode);
}
