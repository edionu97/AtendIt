package application.controller;

import application.messages.ErrorMessage;
import application.messages.request.CheckStudentFaceMessage;
import application.service.StudentAttendenceService;
import application.service.interfaces.IStudentAttendenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attendance")
@ComponentScan(basePackages = "application.service")
public class StudentAttendanceController {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public StudentAttendanceController(final IStudentAttendenceService attendenceService) {
        this.studentAttendenceService = attendenceService;
    }

    @PostMapping(value = "/check")
    public ResponseEntity<?> checkStudent(@RequestBody CheckStudentFaceMessage message) {

        if(message.getUsername() == null){
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        final boolean result = studentAttendenceService.hasFaceSet(message.getUsername());
        return new ResponseEntity<>(
                result ? HttpStatus.FOUND : HttpStatus.NOT_FOUND
        );
    }


    private IStudentAttendenceService studentAttendenceService;
}
