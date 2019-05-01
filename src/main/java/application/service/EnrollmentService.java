package application.service;

import application.database.interfaces.IEnrollmentRepo;
import application.database.interfaces.IUserRepo;
import application.messages.ErrorMessage;
import application.model.Course;
import application.model.Enrollment;
import application.model.User;
import application.service.interfaces.ICourseService;
import application.service.interfaces.IEnrollmentService;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.ClassType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@ComponentScan(
        basePackages = {
                "application.config", "application.service"
        }
)
public class EnrollmentService implements IEnrollmentService {

    @Autowired
    public EnrollmentService(final IUserRepo userRepo, final ICourseService courseService, final IEnrollmentRepo enrollmentRepo) {
        this.userRepo = userRepo;
        this.courseService = courseService;
        this.enrollmentRepo = enrollmentRepo;
    }


    @Override
    public void addEnrollment(
            final String studentName, final String courseName, final ClassType type, final String teacherName, final String group) throws ErrorMessageException {


        final Optional<User> studentOptional = userRepo.findUserByUsername(studentName);

        if (!studentOptional.isPresent()) {
            throw new ErrorMessageException(
                    String.format("User with username: %s not found", studentName), HttpStatus.NOT_FOUND
            );
        }

        final Optional<Course> courseOptional = courseService.findCourseBy(
                teacherName, courseName, type
        );

        if (!courseOptional.isPresent()) {
            throw new ErrorMessageException(
                    String.format("Course (%s, %s, %s) not found", teacherName, courseName, type), HttpStatus.NOT_FOUND
            );
        }

        enrollmentRepo.addEnrollment(
                studentOptional.get(), courseOptional.get(), group
        );
    }

    @Override
    public void deleteEnrollment(
            final String studentName, final String courseName, final ClassType type, final String teacherName) throws ErrorMessageException {

        final Optional<User> studentOptional = userRepo.findUserByUsername(studentName);

        final Optional<Course> courseOptional = courseService.findCourseBy(
                teacherName, courseName, type
        );

        if (!studentOptional.isPresent()) {
            throw new ErrorMessageException(
                    String.format("User with username: %s not found", studentName), HttpStatus.NOT_FOUND
            );
        }

        if (!courseOptional.isPresent()) {

            throw new ErrorMessageException(
                    String.format("Course: (%s, %s, %s) not found", teacherName, courseName, type), HttpStatus.NOT_FOUND
            );
        }


        final Optional<Enrollment> enrollment = enrollmentRepo.findEnrollmentByUserAndCourse(
                studentOptional.get(), courseOptional.get()
        );

        if (!enrollment.isPresent()) {
            throw new ErrorMessageException(
                    String.format(
                            "Enrollment not found for user %s at (%s, %s, %s)",
                            studentName, teacherName, courseName, type
                    ), HttpStatus.NOT_FOUND
            );
        }

        enrollmentRepo.delete(enrollment.get());
    }

    @Override
    public boolean isEnrolledAtCourse(String studentName, String courseName, ClassType type, String teacherName) {

        final Optional<User> studentOptional = userRepo.findUserByUsername(studentName);

        final Optional<Course> courseOptional = courseService.findCourseBy(
                teacherName, courseName, type
        );

        return studentOptional.filter(user -> courseOptional.filter(course -> enrollmentRepo.findEnrollmentByUserAndCourse(user, course).isPresent()).isPresent()).isPresent();

    }

    @Override
    public List<Enrollment> getEnrollmentsFor(final String studentName) {
        return enrollmentRepo
                .getEnrollmentsFor(studentName)
                .stream()
                .peek(
                        enrollment -> enrollment.getCourse().getUser().setPassword(null)
                )
                .collect(Collectors.toList());
    }


    private IUserRepo userRepo;
    private IEnrollmentRepo enrollmentRepo;
    private ICourseService courseService;
}
