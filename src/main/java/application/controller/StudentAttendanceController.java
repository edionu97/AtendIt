package application.controller;

import application.messages.ErrorMessage;
import application.messages.request.CheckStudentFaceMessage;
import application.messages.request.GetCoursesForUserMessage;
import application.messages.response.GetCoursesResponse;
import application.model.Course;
import application.service.interfaces.IAttendanceService;
import application.service.interfaces.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/attendance")
@ComponentScan(basePackages = "application.service")
public class StudentAttendanceController {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public StudentAttendanceController(final IAttendanceService attendanceService, final ICourseService courseService) {
        this.attendanceService = attendanceService;
        this.courseService = courseService;
    }

    @PostMapping(value = "/check")
    public ResponseEntity<?> checkStudent(@RequestBody CheckStudentFaceMessage message) {

        if (message.getUsername() == null) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        final boolean result = attendanceService.hasFaceSet(message.getUsername());
        return new ResponseEntity<>(
                result ? HttpStatus.FOUND : HttpStatus.NOT_FOUND
        );
    }

    @GetMapping(value = "/courses")
    public ResponseEntity<?> getCourses() {

        try {
            final List<Course> courseList = new ArrayList<>(courseService.getAll());
            return new ResponseEntity<>(
                    new GetCoursesResponse(courseList), HttpStatus.OK
            );
        } catch (Exception ex) {
            ErrorMessage errorMessage = new ErrorMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage()
            );
            return new ResponseEntity<>(errorMessage, errorMessage.getCode());
        }
    }

    private IAttendanceService attendanceService;
    private ICourseService courseService;
}
