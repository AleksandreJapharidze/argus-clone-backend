package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.lecture.CreateLectureRequest;
import com.example.argusclone.dtos.lecture.LectureResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Group;
import com.example.argusclone.entities.Lecture;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.exceptions.ScheduleConflictException;
import com.example.argusclone.mappers.LectureMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.GroupRepository;
import com.example.argusclone.repositories.LectureRepository;
import com.example.argusclone.services.LectureService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LectureServiceImpl implements LectureService {
    private static final int SEMESTER_WEEKS = 15;

    private final LectureRepository lectureRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final LectureMapper lectureMapper;

    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository,
                              GroupRepository groupRepository,
                              CourseRepository courseRepository,
                              LectureMapper lectureMapper) {
        this.lectureRepository = lectureRepository;
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
        this.lectureMapper = lectureMapper;
    }

    @Override
    @Cacheable(value = "LECTURE_CACHE", key = "'groupId: ' + #groupId")
    public List<LectureResponse> getLecturesForGroup(Integer groupId) {
        return lectureRepository.findByGroupId(groupId)
                .stream()
                .map(lectureMapper::toResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "LECTURE_CACHE", key = "'studentId: ' + #studentId + ', date: ' + #lectureDate")
    public List<LectureResponse> getLecturesByLectureDateForStudent(Integer studentId, LocalDate lectureDate) {
        return lectureRepository.findLecturesByLectureDateForStudent(studentId, lectureDate)
                .stream()
                .map(lectureMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = "LECTURE_CACHE", key = "'groupId: ' + #groupId")
    @CachePut(value = "LECTURE_CACHE", key = "'groupId: ' + #groupId")
    public List<LectureResponse> addLecturesToGroup(Integer groupId, List<CreateLectureRequest> lectures) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new ResourceNotFoundException("Group with an id of " + groupId + " not found")
        );

        List<Lecture> newLectures = generateLecturesForTheSemester(lectures);
        newLectures.forEach(lecture -> lecture.setGroup(group));

        try {
            lectureRepository.saveAll(newLectures);
            lectureRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ScheduleConflictException("Lecture or lectures conflict with an existing scheduled lecture");
        }

        return newLectures
                .stream()
                .map(lectureMapper::toResponse)
                .toList();
    }

    private List<Lecture> generateLecturesForTheSemester(List<CreateLectureRequest> lectures) {
        validateNoLectureCollisions(lectures);

        List<Lecture> newLectures = new ArrayList<>();

        int weeksAdded = 0;
        int i = 0;

        while (weeksAdded < SEMESTER_WEEKS) {
            for (CreateLectureRequest lecture : lectures) {

                Lecture newLecture = lectureMapper.toEntity(lecture);
                LocalDate date = lecture.getLectureDate().plusWeeks(i);

                if (isHoliday(date)) {
                    continue;
                }

                newLecture.setLectureDate(date);
                newLecture.setLectureStartTime(lecture.getLectureStartTime());
                newLecture.setLectureEndTime(lecture.getLectureEndTime());
                newLecture.setRoomNumber(lecture.getRoomNumber());

                newLectures.add(newLecture);
            }

            weeksAdded++;
            i++;
        }

        return newLectures;
    }

    private void validateNoLectureCollisions(List<CreateLectureRequest> lectures) {
        long distinctCount = lectures.stream().distinct().count();
        if (distinctCount != lectures.size()) {
            throw new DuplicateResourceException("Two or more lectures collide with each other.");
        }
    }

    private boolean isHoliday(LocalDate date) {
        return date.isAfter(LocalDate.of(2025, 12, 24)) && date.isBefore(LocalDate.of(2026, 1, 8));
    }

    @Override
    @Transactional
    @CacheEvict(value = "LECTURE_CACHE", allEntries = true)
    public void deleteLecturesByGroupId(Integer groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new ResourceNotFoundException("Group with an id of " + groupId + " not found");
        }

        lectureRepository.deleteByGroupId(groupId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "LECTURE_CACHE", allEntries = true)
    public void deleteLecturesByCourseId(Integer courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        course.getGroups().forEach(group -> lectureRepository.deleteByGroupId(group.getId()));
    }
}
