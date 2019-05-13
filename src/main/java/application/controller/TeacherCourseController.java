package application.controller;
import application.messages.ErrorMessage;
import application.messages.request.CourseByMessage;
import application.messages.request.GetCoursesForUserMessage;
import application.messages.response.FindCourseResponse;
import application.messages.response.GetCoursesResponse;
import application.model.Course;
import application.service.interfaces.ICourseService;
import application.utils.exceptions.ErrorMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
 @RestController
@RequestMapping("/courses")
@ComponentScan(basePackages = "application.service")
public class TeacherCourseController {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public TeacherCourseController(final ICourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping(value = "/posted-by")
    public ResponseEntity<?> getCoursesPostedByUser(@RequestBody GetCoursesForUserMessage message){

        if(message.getUsername() == null || message.getUsername().isEmpty()){
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            return new ResponseEntity<>(
                    new GetCoursesResponse(
                            courseService.getCoursesFor(message.getUsername())
                    ), HttpStatus.OK
            );
        } catch (Exception ex) {
            ErrorMessage errorMessage = new ErrorMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage()
            );
            return new ResponseEntity<>(errorMessage, errorMessage.getCode());
        }
    }

    @PostMapping(value = "/find-by")
    public ResponseEntity<?> getCoursesPostedByUser(@RequestBody CourseByMessage message){

        if(message.getUsername().isEmpty() || message.getType() == null || message.getName().isEmpty()){
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            final Optional<Course> course = courseService.findCourseBy(
                message.getUsername(), message.getName(), message.getType()
            );

            return course
                    .<ResponseEntity<?>>map(
                            course1 -> new ResponseEntity<>(new FindCourseResponse(course1), HttpStatus.OK)
                    )
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception ex) {
            ErrorMessage errorMessage = new ErrorMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage()
            );
            return new ResponseEntity<>(errorMessage, errorMessage.getCode());
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> addCourse(@RequestBody CourseByMessage message){

        if(message.getUsername().isEmpty() || message.getType() == null || message.getName().isEmpty()){
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
           courseService.addCourse(
                   message.getUsername(), message.getName(), message.getType()
           );
        } catch (ErrorMessageException ex) {
            return new ResponseEntity<>(
                    ex.getErrorMessage(), ex.getErrorMessage().getCode()
            );
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private ICourseService courseService;
}
