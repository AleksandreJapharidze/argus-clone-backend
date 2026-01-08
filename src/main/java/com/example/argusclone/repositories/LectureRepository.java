package com.example.argusclone.repositories;

import com.example.argusclone.entities.Lecture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    @EntityGraph(attributePaths = {"group"})
    @Query("SELECT l FROM Group g JOIN g.lectures l WHERE g.id = :groupId")
    List<Lecture> findByGroupId(@Param("groupId") Integer groupId);

    @EntityGraph(attributePaths = {"group"})
    @Query("SELECT l FROM Group g JOIN g.lectures l JOIN g.students s WHERE s.id = :studentId AND l.lectureDate = :date")
    List<Lecture> findLecturesByLectureDateForStudent(@Param("studentId") Integer studentId,  @Param("date") LocalDate date);

    @Modifying
    @Query("DELETE FROM Lecture l WHERE l.group.id = :groupId")
    void deleteByGroupId(@Param("groupId") Integer groupId);
}
