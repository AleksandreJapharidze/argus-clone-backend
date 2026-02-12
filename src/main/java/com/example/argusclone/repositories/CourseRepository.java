package com.example.argusclone.repositories;

import com.example.argusclone.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Override
    @EntityGraph(attributePaths = {"instructors"})
    Optional<Course> findById(Integer id);

    @Override
    @EntityGraph(attributePaths = {"instructors"})
    Page<Course> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"instructors"})
    @Query("SELECT DISTINCT c FROM Course c WHERE " +
            "LOWER(c.courseName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :keyword, '%'))"
    )
    List<Course> searchCourses(String keyword);

    @EntityGraph(attributePaths = {"instructors"})
    Optional<Course> findByCourseCode(String courseCode);

    // The JOIN clauses are just navigations to reach Course from Student via Group.
    @EntityGraph(attributePaths = "instructors")
    @Query("SELECT DISTINCT c FROM Student s JOIN s.groups g JOIN g.course c WHERE s.id = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") Integer studentId);

    @EntityGraph(attributePaths = "instructors")
    @Query("SELECT DISTINCT c FROM Course c JOIN c.instructors i WHERE i.id = :instructorId")
    List<Course> findAllByInstructorId(@Param("instructorId") Integer instructorId);
}
