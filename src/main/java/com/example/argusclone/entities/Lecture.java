package com.example.argusclone.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
        name = "lecture",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_lecture_slot",
                columnNames = {"lecture_date", "lecture_start_time", "lecture_end_time", "room_number"}
        )
)
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lecture_date", nullable = false)
    private LocalDate lectureDate;

    @Column(name = "lecture_start_time", nullable = false)
    private LocalTime lectureStartTime;

    @Column(name = "lecture_end_time", nullable = false)
    private LocalTime lectureEndTime;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    public Integer getId() {
        return id;
    }

    public LocalDate getLectureDate() {
        return lectureDate;
    }

    public void setLectureDate(LocalDate lectureDate) {
        this.lectureDate = lectureDate;
    }

    public LocalTime getLectureStartTime() {
        return lectureStartTime;
    }

    public void setLectureStartTime(LocalTime lectureStartTime) {
        this.lectureStartTime = lectureStartTime;
    }

    public LocalTime getLectureEndTime() {
        return lectureEndTime;
    }

    public void setLectureEndTime(LocalTime lectureEndTime) {
        this.lectureEndTime = lectureEndTime;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
