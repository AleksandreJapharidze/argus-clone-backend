package com.example.argusclone.repositories;

import com.example.argusclone.entities.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    Optional<Lecture> findByLectureDateAndLectureStartTimeAndLectureEndTimeAndRoomNumber(
            LocalDate lectureDate, LocalTime lectureStartTime, LocalTime lectureEndTime, String roomNumber
    );
//    List<Lecture> findByLectureDateAndRoomNumber(LocalDate date, String roomNumber);
    void deleteLecturesByCourseId(Integer courseId);
}
