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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class LectureServiceImpl implements LectureService {
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
        if (theseLecturesOverlapStreamVersion(lectures)) {
            throw new ScheduleConflictException("Two or more of these lectures overlap.");
        }

        List<LocalDate> lectureDates = lectures.stream().map(lectureRequest ->
                        getDateOfTheDayOfTheWeek(lectureRequest.getDayOfWeek().toUpperCase()))
                .toList();


        validateNoLectureCollisions(lectures);

        if (overlappingLectureOrLecturesExistInDatabase(lectures, lectureDates)) {
            throw new ScheduleConflictException("One or more of the existing lectures overlap with these lecture(s).");
        }

        List<Lecture> newLectures = new ArrayList<>();
        LocalDate lastSemesterDay = getDateOfTheLastSemesterDay();

        int weekOffset = 0;

        while (true) {
            boolean anyLectureGeneratedThisWeek = false;

            for (int i = 0; i < lectures.size(); i++) {
                CreateLectureRequest lecture = lectures.get(i);
                LocalDate date = lectureDates.get(i).plusWeeks(weekOffset);

                if (date.isAfter(lastSemesterDay)) {
                    continue;
                }

                if (isHoliday(date)) {
                    continue;
                }

                Lecture newLecture = createLecture(lecture, date);

                newLectures.add(newLecture);
                anyLectureGeneratedThisWeek = true;
            }

            if (!anyLectureGeneratedThisWeek) {
                break;
            }

            weekOffset++;
        }

        return newLectures;
    }

    private Lecture createLecture(CreateLectureRequest lectureRequest, LocalDate date) {
        Lecture lecture = lectureMapper.toEntity(lectureRequest);
        lecture.setLectureDate(date);
        lecture.setLectureStartTime(lectureRequest.getLectureStartTime());
        lecture.setLectureEndTime(lectureRequest.getLectureEndTime());
        lecture.setRoomNumber(lectureRequest.getRoomNumber());
        return lecture;
    }

    private void validateNoLectureCollisions(List<CreateLectureRequest> lectures) {
        long distinctCount = lectures.stream().distinct().count();
        if (distinctCount != lectures.size()) {
            throw new DuplicateResourceException("Two or more lectures collide with each other.");
        }
    }

    private boolean overlappingLectureOrLecturesExistInDatabase(List<CreateLectureRequest> lectures, List<LocalDate> lectureDates) {
        for (int i = 0; i < lectures.size(); i++) {
            CreateLectureRequest lecture = lectures.get(i);
            LocalDate date = lectureDates.get(i);

            if (lectureRepository.existOverlappingLectureOrLectures(
                    date, lecture.getLectureStartTime(), lecture.getLectureEndTime(), lecture.getRoomNumber()
            )) {
                return true;
            }
        }
        return false;
    }

    private boolean theseLecturesOverlapNormalVersion(List<CreateLectureRequest> lectures) {
        for (int i = 0; i < lectures.size(); i++) {
            for (int j = i + 1; j < lectures.size(); j++) {

                CreateLectureRequest l1 = lectures.get(i);
                CreateLectureRequest l2 = lectures.get(j);

                if (l1.getDayOfWeek().equals(l2.getDayOfWeek()) &&
                        l1.getRoomNumber().equals(l2.getRoomNumber()) &&
                        l1.getLectureStartTime().isBefore(l2.getLectureEndTime()) &&
                        l2.getLectureStartTime().isBefore(l1.getLectureEndTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean theseLecturesOverlapStreamVersion(List<CreateLectureRequest> lectures) {
        return lectures.stream().anyMatch(l1 -> lectures.stream()
                .anyMatch(l2 -> {
                    if (l1 == l2) {
                        return false;
                    }
                    return l1.getDayOfWeek().equals(l2.getDayOfWeek()) &&
                            l1.getRoomNumber().equals(l2.getRoomNumber()) &&
                            l1.getLectureStartTime().isBefore(l2.getLectureEndTime()) &&
                            l2.getLectureStartTime().isBefore(l1.getLectureEndTime());
                })
        );
    }

    private LocalDate getDateOfTheDayOfTheWeek(String dayOfWeek) {
        DayOfWeek day = DayOfWeek.valueOf(dayOfWeek);

        int currentMonthValue = LocalDate.now().getMonth().getValue();
        if (currentMonthValue == 7 || currentMonthValue == 8 || currentMonthValue == 9 ||
                currentMonthValue == 10 || currentMonthValue == 11 || currentMonthValue == 12) {
            YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), Month.SEPTEMBER);
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

            LocalDate lastSunday = lastDayOfMonth.with(DayOfWeek.SUNDAY);
            LocalDate startOfLastFullWeek = lastSunday.with(lastSunday.minusDays(6));

            return startOfLastFullWeek.with(day);
        } else {
            YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), Month.MARCH);
            LocalDate firstDayOfMonth = yearMonth.atDay(1);

            LocalDate startOfFirstFullWeek = firstDayOfMonth.with(DayOfWeek.MONDAY);
            if (startOfFirstFullWeek.getMonth() != Month.MARCH) {
                startOfFirstFullWeek = startOfFirstFullWeek.plusWeeks(1);
            }

            return startOfFirstFullWeek.with(day);
        }
    }

    private LocalDate getDateOfTheLastSemesterDay() {
        int currentMonthValue = LocalDate.now().getMonth().getValue();
        if (currentMonthValue == 7 || currentMonthValue == 8 || currentMonthValue == 9 ||
                currentMonthValue == 10 || currentMonthValue == 11 || currentMonthValue == 12) {
            YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear() + 1, Month.FEBRUARY);
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

            return lastDayOfMonth.minusWeeks(2).with(DayOfWeek.SATURDAY);
        } else {
            YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), Month.JULY);
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

            return lastDayOfMonth.minusWeeks(2).with(DayOfWeek.SATURDAY);
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
