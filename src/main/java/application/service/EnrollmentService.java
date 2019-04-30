package application.service;

import application.database.interfaces.IEnrollmentRepo;
import application.database.interfaces.IUserRepo;
import application.model.Course;
import application.model.Enrollment;
import application.model.User;
import application.service.interfaces.ICourseService;
import application.service.interfaces.IEnrollmentService;
import application.utils.model.ClassType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
            final String studentName, final String courseName, final ClassType type, final String teacherName, final String group) throws Exception {


        final Optional<User> studentOptional = userRepo.findUserByUsername(studentName);

        if(!studentOptional.isPresent()){
            throw  new Exception(
                    String.format("User with username: %s not found", studentName)
            );
        }

        final Optional<Course> courseOptional = courseService.findCourseBy(
                teacherName, courseName, type
        );

        if(!courseOptional.isPresent()){
            throw  new Exception(
                    String.format("Course (%s, %s, %s) not found", teacherName, courseName, type)
            );
        }

        enrollmentRepo.addEnrollment(
                studentOptional.get(), courseOptional.get(), group
        );
    }

    @Override
    public void deleteEnrollment(
            final String studentName, final String courseName, final ClassType type, final String teacherName) throws Exception {

        final Optional<User> studentOptional = userRepo.findUserByUsername(studentName);

        final Optional<Course> courseOptional = courseService.findCourseBy(
                teacherName, courseName, type
        );

        if(!studentOptional.isPresent()){
            throw  new Exception(
                    String.format("User with username: %s not found", studentName)
            );
        }

        if(!courseOptional.isPresent()){
            throw  new Exception(
                    String.format("Course (%s, %s, %s) not found", teacherName, courseName, type)
            );
        }

        final Optional<Enrollment> enrollment = enrollmentRepo.findEnrollmentByUserAndCourse(
                studentOptional.get(), courseOptional.get()
        );

        if(!enrollment.isPresent()){
            throw  new Exception(
                    String.format(
                            "Enrollment not found for user %s at (%s, %s, %s)",
                            studentName, teacherName, courseName, type
                    )
            );
        }

        enrollmentRepo.delete(enrollment.get());
    }

    @Override
    public List<Enrollment> getEnrollmentsFor(final String studentName) {
        return enrollmentRepo.getEnrollmentsFor(studentName);
    }


    private IUserRepo userRepo;
    private IEnrollmentRepo enrollmentRepo;
    private ICourseService courseService;
}
