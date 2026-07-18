package com.example.argusclone.repositories;

import com.example.argusclone.entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
    Optional<Instructor> findByName(String name);
    Optional<Instructor> findByEmail(String email);

    @Query("SELECT i.id FROM Instructor i JOIN i.courses c WHERE c.id = :courseId")
    List<Integer> findInstructorIdsByCourseId(Integer courseId);
}
