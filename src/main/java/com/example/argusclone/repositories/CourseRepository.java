package com.example.argusclone.repositories;

import com.example.argusclone.dtos.course.CourseResponse;
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
    Optional<Course> getCourseById(Integer id);

    @Override
    @EntityGraph(attributePaths = {"instructors"})
    Page<Course> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"instructors"})
    @Query("SELECT DISTINCT c FROM Course c WHERE " +
            "LOWER(c.courseName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :keyword, '%'))"
    )
    List<CourseResponse> searchCourses(String keyword);

    @EntityGraph(attributePaths = {"instructors"})
    Optional<Course> findByCourseCode(String courseCode);

    // The JOIN clauses are just navigations to reach Course from Student via Group.
    @EntityGraph(attributePaths = "instructors")
    @Query("SELECT DISTINCT c FROM Student s JOIN s.groups g JOIN g.course c WHERE s.id = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") Integer studentId);

    @EntityGraph(attributePaths = "instructors")
    @Query("SELECT DISTINCT c FROM Course c JOIN c.instructors i WHERE i.id = :instructorId")
    List<Course> findAllByInstructorId(@Param("instructorId") Integer instructorId);

    @Query("SELECT DISTINCT c.id FROM Course c JOIN c.instructors i WHERE i.id = :instructorId")
    List<Integer> findAllCourseIdsByInstructorId(Integer instructorId);

//    String query = "SELECT DISTINCT c.id FROM Course c JOIN c.groups g JOIN g.students s WHERE s.id = :studentId";

    @Query("SELECT DISTINCT c.id FROM Course c JOIN c.groups g JOIN g.students s WHERE s.id = :studentId")
    List<Integer> findAllCourseIdsByStudentId(@Param("studentId") Integer studentId);
}
