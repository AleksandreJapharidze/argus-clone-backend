package com.example.argusclone.repositories;

import com.example.argusclone.entities.Lecture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    @EntityGraph(attributePaths = {"group"})
    @Query("SELECT l FROM Group g JOIN g.lectures l WHERE g.id = :groupId")
    List<Lecture> findByGroupId(@Param("groupId") Integer groupId);

    @Modifying
    @Query("DELETE FROM Lecture l WHERE l.group.id = :groupId")
    void deleteByGroupId(@Param("groupId") Integer groupId);
}
