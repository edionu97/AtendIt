package application.service;

import application.database.interfaces.IAttendanceRepo;
import application.database.interfaces.IEnrollmentRepo;
import application.database.interfaces.IUserRepo;
import application.model.Attendance;
import application.model.Course;
import application.model.User;
import application.service.interfaces.IAttendanceService;
import application.service.interfaces.ICourseService;
import application.service.interfaces.IEnrollmentService;
import application.utils.model.ClassType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void addAttendance(String studentName, String courseName, ClassType type, String teacherName) throws Exception {

        final Optional<Course> courseOptional = courseService.findCourseBy(
                teacherName, courseName, type
        );

        final Optional<User> studentOptional = userRepo.findUserByUsername(studentName);

        if (!studentOptional.isPresent()) {
            throw new Exception(
                    String.format("User with username: %s not found", studentName)
            );
        }

        if (!courseOptional.isPresent()) {
            throw new Exception(
                    String.format("Course (%s, %s, %s) not found", teacherName, courseName, type)
            );
        }


        if (!enrollmentService.isEnrolledAtCourse(studentName, courseName, type, teacherName)) {
            throw new Exception("Please enroll to this course");
        }

        attendanceRepo.addAttendance(
                studentOptional.get(), courseOptional.get()
        );
    }

    @Override
    public void delete(int attendanceId) throws Exception {

        final Optional<Attendance> attendance = attendanceRepo.findById(attendanceId);

        if (!attendance.isPresent()) {
            throw new Exception("Attendance not found!");
        }

        attendanceRepo.delete(attendance.get());
    }

    @Override
    public void update(Attendance attendance) {
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

        if (!studentOptional.isPresent()) {
            return new ArrayList<>();
        }

        return attendanceRepo.getAllAttendancesFor(studentOptional.get());
    }

    @Override
    public List<Attendance> getAttendanceForAt(String studentName, String courseName, ClassType type, String teacherName) {

        final Optional<Course> courseOptional = courseService.findCourseBy(
                teacherName, courseName, type
        );

        final Optional<User> studentOptional = userRepo.findUserByUsername(studentName);

        if (!studentOptional.isPresent()) {
            return new ArrayList<>();
        }

        if (!courseOptional.isPresent()) {
            return new ArrayList<>();
        }

        return attendanceRepo.getAttendancesForAt(
                studentOptional.get(), courseOptional.get()
        );
    }

    private IUserRepo userRepo;
    private ICourseService courseService;
    private IAttendanceRepo attendanceRepo;
    private IEnrollmentService enrollmentService;
}
