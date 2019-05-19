package application.service;

import application.database.interfaces.IAttendanceRepo;
import application.database.interfaces.IUserRepo;
import application.model.Attendance;
import application.model.Course;
import application.model.User;
import application.service.interfaces.IAttendanceService;
import application.service.interfaces.ICourseService;
import application.service.interfaces.IEnrollmentService;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.ClassType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@ComponentScan(
        basePackages = {
                "application.config", "application.service"
        }
)
public class AttendanceService implements IAttendanceService {

    @Autowired
    public AttendanceService(
            final IUserRepo userRepo, final ICourseService courseService, final IAttendanceRepo attendanceRepo, final IEnrollmentService enrollmentService) {
        this.userRepo = userRepo;
        this.courseService = courseService;
        this.attendanceRepo = attendanceRepo;
        this.enrollmentService = enrollmentService;
    }

    @Override
    public boolean hasFaceSet(final String username) {
        return userRepo.getUserFaceId(username).isPresent();
    }

    @Override
    public void addAttendance(String studentName, String courseName, ClassType type, String teacherName) throws ErrorMessageException {

        final Optional<Course> courseOptional = courseService.findCourseBy(
                teacherName, courseName, type
        );

        final Optional<User> studentOptional = userRepo.findUserByUsername(studentName);

        if (!studentOptional.isPresent()) {
            throw new ErrorMessageException(
                    String.format("User with username: %s not found", studentName), HttpStatus.NOT_FOUND
            );
        }

        if (!courseOptional.isPresent()) {
            throw new ErrorMessageException(
                    String.format("Course (%s, %s, %s) not found", teacherName, courseName, type),
                    HttpStatus.NOT_FOUND
            );
        }


        if (!enrollmentService.isEnrolledAtCourse(studentName, courseName, type, teacherName)) {
            throw new ErrorMessageException(
                    String.format("Student %s must enroll first at course (%s, %s, %s)", studentName, teacherName, courseName, type),
                    HttpStatus.FORBIDDEN
            );
        }

        attendanceRepo.addAttendance(
                studentOptional.get(), courseOptional.get()
        );
    }

    @Override
    public void delete(int attendanceId) throws ErrorMessageException {

        final Optional<Attendance> attendance = attendanceRepo.findById(attendanceId);

        if (!attendance.isPresent()) {
            throw new ErrorMessageException("Attendance not found", HttpStatus.NOT_FOUND);
        }

        attendanceRepo.delete(attendance.get());
    }

    @Override
    public void update(Attendance attendance) throws ErrorMessageException {
        attendanceRepo.update(attendance);
    }

    @Override
    public Optional<Attendance> findById(int id) {
        return attendanceRepo.findById(id);
    }

    @Override
    public List<Attendance> getAll() {
        return attendanceRepo.getAll();
    }

    @Override
    public List<Attendance> getAttendanceFor(String username) {

        final Optional<User> studentOptional = userRepo.findUserByUsername(username);
        return studentOptional.map(
                user -> attendanceRepo
                        .getAllAttendancesFor(user)
                        .stream()
                        .peek(
                                attendance -> attendance.getCourse().getUser().setPassword(null)
                        )
                        .collect(Collectors.toList())
        ).orElseGet(ArrayList::new);
    }

    @Override
    public List<Attendance> getAttendanceForAt(String studentName, String courseName, ClassType type, String teacherName) {

        final Optional<Course> courseOptional = courseService.findCourseBy(
                teacherName, courseName, type
        );

        final Optional<User> studentOptional = userRepo.findUserByUsername(studentName);

        return studentOptional
                .map(
                        user -> courseOptional
                                .map(course -> attendanceRepo.getAttendancesForAt(user, course)
                                        .stream()
                                        .peek(
                                                attendance -> attendance.getCourse().getUser().setPassword(null)
                                        )
                                        .collect(Collectors.toList())
                                ).orElseGet(ArrayList::new)
                ).orElseGet(ArrayList::new);
    }

    private IUserRepo userRepo;
    private ICourseService courseService;
    private IAttendanceRepo attendanceRepo;
    private IEnrollmentService enrollmentService;
}
