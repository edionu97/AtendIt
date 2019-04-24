package application.notifications.stomp.controllers;

import application.notifications.stomp.models.IResponse;
import application.notifications.stomp.models.implementations.TestMessage;
import application.notifications.stomp.models.implementations.TestResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;


//@Controller
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
