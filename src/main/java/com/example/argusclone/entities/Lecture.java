package com.example.argusclone.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate lectureDate;
    private LocalTime lectureStartTime;
    private LocalTime lectureEndTime;

    private String roomNumber;

    @ManyToOne
    @JoinColumn(name = "group_id")
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
