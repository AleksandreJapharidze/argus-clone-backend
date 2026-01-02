package com.example.argusclone.repositories;

import com.example.argusclone.entities.Lecture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    @EntityGraph(attributePaths = {"group"})
    @Query("SELECT l FROM Group g JOIN g.lectures l WHERE g.id = :groupId")
    List<Lecture> findByGroupId(@Param("groupId") Integer groupId);
}
