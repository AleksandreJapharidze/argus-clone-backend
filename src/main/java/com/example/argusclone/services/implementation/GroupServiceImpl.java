package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.group.CreateGroupRequest;
import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.lecture.CreateLectureRequest;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Group;
import com.example.argusclone.entities.Lecture;
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
        Group group = groupRepository.findById(id).orElseThrow();
        return groupMapper.toResponse(group);
    }

    @Override
    public GroupResponse createGroup(Integer courseId, CreateGroupRequest group) {
        Course course = courseRepository.findById(courseId).orElseThrow();

        Group newGroup = groupMapper.toEntity(group);
        newGroup.setCourse(course);

        Group saved = groupRepository.save(newGroup);
        return groupMapper.toResponse(saved);
    }

    @Override
    public GroupResponse addLecturesToGroup(Integer groupId, List<CreateLectureRequest> lectures) {
        Group group = groupRepository.findById(groupId).orElseThrow();

        List<Lecture> newLectures = generateLecturesForTheSemester(lectures);

        lectureRepository.saveAll(newLectures);
        group.setLectures(newLectures);

        return groupMapper.toResponse(groupRepository.save(group));
    }

    private List<Lecture> generateLecturesForTheSemester(List<CreateLectureRequest> lectures) {
        validateNoConflicts(lectures);

        List<Lecture> newLectures = new ArrayList<>();
        for (int i=0; i<=14; i++) {
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
                throw new RuntimeException("Lecture conflicts with some other lecture"); // Exception handling will be implemented later
            }
        }
    }

    @Override
    public void deleteGroupById(Integer id) {
        if (!groupRepository.existsById(id)) {
            return;
        }

        groupRepository.deleteById(id);
    }
}
