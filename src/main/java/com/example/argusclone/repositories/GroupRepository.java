package com.example.argusclone.repositories;

import com.example.argusclone.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    List<Group> findByCourseId(Integer courseId);
}
