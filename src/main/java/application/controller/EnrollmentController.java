package application.controller;

import application.messages.ErrorMessage;
import application.messages.request.AddEnrollmentMessage;
import application.messages.request.CourseByMessage;
import application.messages.request.EnrollMessage;
import application.messages.request.GetEnrollmentsForMessage;
import application.messages.response.GetEnrollmentsResponse;
import application.model.Course;
import application.model.Enrollment;
import application.service.interfaces.ICourseService;
import application.service.interfaces.IEnrollmentService;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.ClassType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/enrollment")
@ComponentScan(basePackages = "application.service")
public class EnrollmentController {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public EnrollmentController(final IEnrollmentService enrollmentService, final ICourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
    }


    @PostMapping(value = "/for")
    public ResponseEntity<?> getEnrollmentsFor(@RequestBody GetEnrollmentsForMessage message) {

        if (message.getUsername().isEmpty()) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        final Map<String, List<Enrollment>> enrollments = enrollmentService.getEnrollmentsFor(message.getUsername());
        return new ResponseEntity<>(
                new GetEnrollmentsResponse(enrollments), HttpStatus.OK
        );
    }

    @PostMapping(value = "/at")
    public ResponseEntity<?> getCoursesPostedByUser(@RequestBody CourseByMessage message) {

        if (message.getUsername() == null || message.getType() == null || message.getName() == null) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        final Map<String, List<Enrollment>> enrolled = new HashMap<>();
        enrolled.put(
                "enrollments",
                courseService.getEnrollmentsAtCourse(
                        message.getUsername(), message.getName(), message.getType()));

        return new ResponseEntity<>(enrolled, HttpStatus.OK);
    }

    @PostMapping(value = "/check-enrollment")
    public ResponseEntity<?> isEnrolledAt(@RequestBody EnrollMessage message) {

        if (message.getStudentName().isEmpty() || message.getCourseName().isEmpty() || message.getType() == null || message.getTeacherName().isEmpty()) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        final boolean result = enrollmentService.isEnrolledAtCourse(
                message.getStudentName(), message.getCourseName(), message.getType(), message.getTeacherName()
        );

        return new ResponseEntity<>(
                result ? HttpStatus.FOUND : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> isEnrolledAt(@RequestBody AddEnrollmentMessage message) {

        if (message.getStudentName().isEmpty() || message.getCourseName().isEmpty() || message.getType() == null || message.getTeacherName().isEmpty() || message.getGroup().isEmpty()) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {

            enrollmentService.addEnrollment(
                    message.getStudentName(), message.getCourseName(), message.getType(), message.getTeacherName(), message.getGroup()
            );

        } catch (ErrorMessageException ex) {
            return new ResponseEntity<>(ex.getErrorMessage(), ex.getErrorMessage().getCode());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<?> cancelEnrollment(@RequestBody EnrollMessage message) {

        if (message.getCourseName().isEmpty() || message.getStudentName().isEmpty() || message.getType() == null || message.getTeacherName().isEmpty()) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            enrollmentService.deleteEnrollment(
                    message.getStudentName(), message.getCourseName(), message.getType(), message.getTeacherName()
            );
        } catch (ErrorMessageException ex) {
            return new ResponseEntity<>(ex.getErrorMessage(), ex.getErrorMessage().getCode());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/at-types")
    public ResponseEntity<?> getCourseTypeEnrollmentFor(@RequestBody EnrollMessage message) {

        if (message.getCourseName().isEmpty() || message.getStudentName().isEmpty() || message.getTeacherName().isEmpty()) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(enrollmentService.getEnrollAtWholeCourseType(
                message.getStudentName(), message.getTeacherName(), message.getCourseName()
        ), HttpStatus.OK);
    }

    private IEnrollmentService enrollmentService;
    private ICourseService courseService;
}
