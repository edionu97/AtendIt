package application.controller;

import application.messages.ErrorMessage;
import application.messages.request.*;
import application.messages.response.GetAttendanceForResponse;
import application.messages.response.GetCoursesResponse;
import application.model.Attendance;
import application.model.Course;
import application.service.interfaces.IAttendanceService;
import application.service.interfaces.ICourseService;
import application.utils.exceptions.ErrorMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/for")
    public ResponseEntity<?> getAttendancesFor(@RequestBody GetAttendanceForMessage message) {

        if (message.getUsername() == null) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                new GetAttendanceForResponse(attendanceService.getAttendanceFor(message.getUsername())), HttpStatus.OK);
    }

    @PostMapping("/for-at")
    public ResponseEntity<?> getAttendancesForAt(@RequestBody GetAttendancesForAtMessage message) {

        if (message.getTeacherName() == null || message.getCourseName() == null || message.getType() == null || message.getStudentName() == null) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                new GetAttendanceForResponse(
                        attendanceService.getAttendanceForAt(
                                message.getStudentName(), message.getCourseName(), message.getType(), message.getTeacherName()
                        )),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<?> deleteAttendance(@RequestBody DeleteAttendanceMessage message) {

        if (message.getAttendanceId() <= 0) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Id must be positive"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {

            attendanceService.delete(message.getAttendanceId());
        } catch (ErrorMessageException ex) {
            return new ResponseEntity<>(ex.getErrorMessage(), ex.getErrorMessage().getCode());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/modify")
    public ResponseEntity<?> modifyAttendance(@RequestBody ModifyAttendance message) {

        if (message.getHistoryId() <= 0 || message.getCourseName() == null || message.getPresents() == null || message.getTeacherName() == null || message.getType() == null) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            attendanceService.modifyAttendance(
                message.getCourseName(), message.getType(), message.getTeacherName(), message.getHistoryId(), message.getPresents()
            );
        } catch (ErrorMessageException ex) {
            return new ResponseEntity<>(ex.getErrorMessage(), ex.getErrorMessage().getCode());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private IAttendanceService attendanceService;
    private ICourseService courseService;
}
