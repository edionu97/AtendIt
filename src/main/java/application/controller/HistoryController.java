package application.controller;

import application.messages.ErrorMessage;
import application.messages.request.GetProfileInfoMessage;
import application.model.History;
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
    public ResponseEntity<?> getHistoryFor(@RequestBody GetProfileInfoMessage userMessage){

        if(userMessage.getUsername() == null || userMessage.getUsername().isEmpty()) {
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

    private IHistoryService historyService;
}
