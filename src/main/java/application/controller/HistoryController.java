package application.controller;

import application.messages.ErrorMessage;
import application.messages.request.AbsentsMessage;
import application.messages.request.GetProfileInfoMessage;
import application.messages.request.HistoryForAtMessage;
import application.model.History;
import application.model.User;
import application.service.interfaces.IHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/history")
@ComponentScan(basePackages = "application.service")
public class HistoryController {

    @Autowired
    public HistoryController(final IHistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping(value = "/for")
    public ResponseEntity<?> getHistoryFor(@RequestBody GetProfileInfoMessage userMessage) {

        if (userMessage.getUsername() == null || userMessage.getUsername().isEmpty()) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Username field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        final Map<String, List<History>> history = new HashMap<>();
        history.put(
                "history", historyService.getAllFor(userMessage.getUsername())
        );

        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @PostMapping(value = "/for-at")
    public ResponseEntity<?> getHistoryForAt(@RequestBody HistoryForAtMessage message) {

        if (message.getUsern() == null || message.getId() == 0) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        final Map<String, List<History>> history = new HashMap<>();
        history.put(
                "history", historyService.getAllForAt(message.getUsern(), message.getId())
        );

        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @PostMapping(value = "/absents-at")
    public ResponseEntity<?> getAbsentsAt(@RequestBody AbsentsMessage message) {

        if (message.getCourseName() == null || message.getCourseType() == null || message.getGrupa() == null || message.getTeacherName() == null || message.getHistoryId() <= 0) {
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Required field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        //get response
        final Map<String, List<User>> response = new HashMap<>();
        response.put(
                "absents", historyService.getAbsentUsers(
                        message.getTeacherName(), message.getCourseName(), message.getCourseType(), message.getGrupa(), message.getHistoryId()
                )
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private IHistoryService historyService;
}
