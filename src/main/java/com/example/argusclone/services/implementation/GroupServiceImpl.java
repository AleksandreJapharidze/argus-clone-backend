package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.group.CreateGroupRequest;
import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.lecture.CreateLectureRequest;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Group;
import com.example.argusclone.entities.Lecture;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.exceptions.ScheduleConflictException;
import com.example.argusclone.mappers.GroupMapper;
import com.example.argusclone.mappers.LectureMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.GroupRepository;
import com.example.argusclone.repositories.LectureRepository;
import com.example.argusclone.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private static final int SEMESTER_WEEKS = 15;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private LectureMapper lectureMapper;

    @Override
    public List<GroupResponse> getGroupsForCourse(Integer courseId) {
        return groupRepository.findByCourseId(courseId)
                .stream()
                .map(groupMapper::toResponse)
                .toList();
    }

    @Override
    public GroupResponse getGroupById(Integer id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Group with an id of " + id + " not found")
        );
        return groupMapper.toResponse(group);
    }

    @Override
    public GroupResponse createGroup(Integer courseId, CreateGroupRequest group) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        Group newGroup = groupMapper.toEntity(group);
        newGroup.setCourse(course);

        Group saved = groupRepository.save(newGroup);
        return groupMapper.toResponse(saved);
    }

    @Override
    public GroupResponse addLecturesToGroup(Integer groupId, List<CreateLectureRequest> lectures) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new ResourceNotFoundException("Group with an id of " + groupId + " not found")
        );

        List<Lecture> newLectures = generateLecturesForTheSemester(lectures);

        lectureRepository.saveAll(newLectures);
        group.setLectures(newLectures);

        return groupMapper.toResponse(groupRepository.save(group));
    }

    private List<Lecture> generateLecturesForTheSemester(List<CreateLectureRequest> lectures) {
        validateNoConflicts(lectures);

        List<Lecture> newLectures = new ArrayList<>();
        for (int i=1; i<=SEMESTER_WEEKS; i++) {
            for (CreateLectureRequest lecture : lectures) {
                Lecture newLecture = lectureMapper.toEntity(lecture);
                newLecture.setLectureDate(lecture.getLectureDate().plusWeeks(i));
                newLecture.setLectureStartTime(lecture.getLectureStartTime());
                newLecture.setLectureEndTime(lecture.getLectureEndTime());
                newLecture.setRoomNumber(lecture.getRoomNumber());
                newLectures.add(newLecture);
            }
        }
        return newLectures;
    }

    private void validateNoConflicts(List<CreateLectureRequest> lectures) {
        for (CreateLectureRequest lecture : lectures) {
            if (lectureRepository.findByLectureDateAndLectureStartTimeAndLectureEndTimeAndRoomNumber(
                    lecture.getLectureDate(), lecture.getLectureStartTime(), lecture.getLectureEndTime(), lecture.getRoomNumber()
            ).isPresent()) {
                throw new ScheduleConflictException("Lecture conflicts with some other lecture");
            }
        }
    }

    @Override
    public void deleteGroupById(Integer id) {
        if (!groupRepository.existsById(id)) {
            throw new ResourceNotFoundException("Group with an id of " + id + " not found");
        }

        groupRepository.deleteById(id);
    }

    @Override
    public void deleteGroupsByCourseId(Integer courseId) {
        groupRepository.findByCourseId(courseId).forEach(groupRepository::delete);
    }

    @Override
    public void deleteLecturesByCourseId(Integer courseId) {
        groupRepository.findByCourseId(courseId).forEach(group -> group.getLectures().forEach(lectureRepository::delete));
    }
}
