package com.example.argusclone.services;

import com.example.argusclone.dtos.lecture.CreateLectureRequest;
import com.example.argusclone.entities.Lecture;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ScheduleConflictException;
import com.example.argusclone.repositories.LectureRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CachePut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class LectureAssignmentService {
    private final LectureRepository lectureRepository;
    private final JdbcTemplate jdbcTemplate;

    public LectureAssignmentService(LectureRepository lectureRepository,
                                    JdbcTemplate jdbcTemplate) {
        this.lectureRepository = lectureRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @CachePut(cacheNames = "group-lectures-cache", key = "#groupId")
    @Transactional
    public String generateLecturesForGroup(Integer groupId, List<CreateLectureRequest> lectures) {
        if (lectureRepository.countByGroupId(groupId) > 0) {
            throw new ScheduleConflictException("Group with id " + groupId + " already has lectures");
        }

        List<Lecture> lecturesForSemester = generateLecturesForSemester(lectures);

        try {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO lecture (lecture_date, lecture_start_time, lecture_end_time, room_number, group_id) VALUES (?, ?, ?, ?, ?)",
                    lecturesForSemester,
                    100,
                    (ps, row) -> {
                        ps.setObject(1, Date.valueOf(row.getLectureDate()));
                        ps.setObject(2, Time.valueOf(row.getLectureStartTime()));
                        ps.setTime(3, Time.valueOf(row.getLectureEndTime()));
                        ps.setString(4, row.getRoomNumber());
                        ps.setInt(5, groupId);
                    }
            );
        } catch (DataIntegrityViolationException e) {
            throw new ScheduleConflictException("Lecture or lectures conflict with an existing scheduled lecture");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "Lectures added to group with id " + groupId + " successfully.";
    }

    private List<Lecture> generateLecturesForSemester(List<CreateLectureRequest> lectures) {
        if (theseLecturesOverlapStreamVersion(lectures)) {
            throw new ScheduleConflictException("Two or more of these lectures overlap.");
        }

        List<LocalDate> lectureDates = lectures.stream()
                .map(lectureRequest -> getDateOfTheDayOfTheWeek(lectureRequest.getDayOfWeek().toUpperCase()))
                .toList();


        validateNoLectureCollisions(lectures);

        if (overlappingLecturesExistInDatabase(lectures, lectureDates)) {
            throw new ScheduleConflictException("One or more of the existing lectures overlap with these lecture(s).");
        }

        LocalDate lastSemesterDay = getDateOfTheLastSemesterDay();
        List<Lecture> newLectures = new ArrayList<>();

        int weekOffset = 0;

        while (true) {
            boolean weekIsRelevant = false;

            for (int i = 0; i < lectures.size(); i++) {
                CreateLectureRequest lecture = lectures.get(i);
                LocalDate date = lectureDates.get(i).plusWeeks(weekOffset);

                if (date.isAfter(lastSemesterDay)) {
                    continue;
                }

                weekIsRelevant = true;

                if (isHoliday(date)) {
                    continue;
                }

                Lecture newLecture = createLecture(lecture, date);
                newLectures.add(newLecture);
            }

            if (!weekIsRelevant) {
                break;
            }

            weekOffset++;
        }

        return newLectures;
    }

    private Lecture createLecture(CreateLectureRequest lectureRequest, LocalDate date) {
        Lecture lecture = new Lecture();
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

    private boolean overlappingLecturesExistInDatabase(List<CreateLectureRequest> lectures, List<LocalDate> lectureDates) {
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
                    if (l1.equals(l2)) {
                        return false;
                    }
                    return l1.getDayOfWeek().equals(l2.getDayOfWeek()) &&
                            l1.getRoomNumber().equals(l2.getRoomNumber()) &&
                            l1.getLectureStartTime().isBefore(l2.getLectureEndTime()) &&
                            l2.getLectureStartTime().isBefore(l1.getLectureEndTime());
                })
        );
    }

    private boolean allStartTimesAreBeforeEndTimes(List<CreateLectureRequest> lectures) {
        return lectures.stream().allMatch(l -> l.getLectureStartTime().isBefore(l.getLectureEndTime()));
    }

    private LocalDate getDateOfTheDayOfTheWeek(String dayOfWeek) {
        DayOfWeek day = DayOfWeek.valueOf(dayOfWeek);

        int currentMonthValue = LocalDate.now().getMonth().getValue();
        LocalDate startOfFirstFullWeek = switch (currentMonthValue) {
            case 7, 8, 9, 10, 11, 12 -> {
                YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), Month.SEPTEMBER);
                LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

                LocalDate lastSunday = lastDayOfMonth.with(DayOfWeek.SUNDAY);
                LocalDate startOfLastFullWeek = lastSunday.with(lastSunday.minusDays(6));

                yield startOfLastFullWeek.with(day);
            }
            case 3, 4, 5, 6, 1, 2 -> {
                YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), Month.MARCH);
                LocalDate firstDayOfMonth = yearMonth.atDay(1);

                LocalDate startOfFirstFullWeek1 = firstDayOfMonth.with(DayOfWeek.MONDAY);
                if (startOfFirstFullWeek1.getMonth() != Month.MARCH) {
                    startOfFirstFullWeek1 = startOfFirstFullWeek1.plusWeeks(1);
                }

                yield startOfFirstFullWeek1.with(day);
            }
            default -> throw new IllegalStateException("Unexpected value: " + currentMonthValue);
        };

        return startOfFirstFullWeek;
    }

    private LocalDate getDateOfTheLastSemesterDay() {
        int currentMonthValue = LocalDate.now().getMonth().getValue();
        LocalDate lastDayOfMonth = switch (currentMonthValue) {
            case 7, 8, 9, 10, 11, 12 -> {
                YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear() + 1, Month.FEBRUARY);
                LocalDate lastDayOfMonth1 = yearMonth.atEndOfMonth();
                yield lastDayOfMonth1.minusWeeks(2).with(DayOfWeek.SATURDAY);
            }
            case 3, 4, 5, 6, 1, 2 -> {
                YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), Month.JULY);
                LocalDate lastDayOfMonth2 = yearMonth.atEndOfMonth();
                yield lastDayOfMonth2.minusWeeks(2).with(DayOfWeek.SATURDAY);
            }
            default -> throw new IllegalStateException("Unexpected value: " + currentMonthValue);
        };

        return lastDayOfMonth;
    }

    private boolean isHoliday(LocalDate date) {
        return (date.getMonthValue() == 12 && date.getDayOfMonth() >= 25) || (date.getMonthValue() == 1 && date.getDayOfMonth() <= 7);
    }
}