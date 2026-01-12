package com.example.argusclone.repositories;

import com.example.argusclone.entities.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    @EntityGraph(attributePaths = {"course"})
    List<Group> findByCourseId(Integer courseId);

    @Query("SELECT COUNT(g) FROM Group g JOIN g.students s WHERE g.course.id = :courseId AND s.id = :studentId")
    long countGroupsForStudentInCourse(Integer courseId, Integer studentId);

    @Modifying
    @Query("DELETE FROM Group g WHERE g.course.id = :courseId")
    void deleteByCourseId(Integer courseId);
}
