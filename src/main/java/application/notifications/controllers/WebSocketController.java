package application.notifications.controllers;

import application.notifications.models.IResponse;
import application.notifications.models.implementations.TestMessage;
import application.notifications.models.implementations.TestResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class WebSocketController {

    /**
     * User should subscribe at /topic/inform and should send data to /app/test
     * @param message: the message that will be broadcasted to all users
     * @return a TestResponse
     */

    @MessageMapping("/test")
    @SendTo("/topic/inform")
    public IResponse processTestMessage(@Payload final TestMessage message){

        return new TestResponse("Notified");
    }

}
