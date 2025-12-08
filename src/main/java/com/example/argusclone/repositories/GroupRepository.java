package com.example.argusclone.repositories;

import com.example.argusclone.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    List<Group> findByCourseId(Integer courseId);

    @Query("SELECT COUNT(g) FROM Group g JOIN g.students s WHERE g.course.id = :courseId AND s.id = :studentId")
    long countGroupsForStudentInCourse(Integer courseId, Integer studentId);
}
