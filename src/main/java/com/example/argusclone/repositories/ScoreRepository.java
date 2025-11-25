package com.example.argusclone.repositories;

import com.example.argusclone.entities.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Integer> {
}
