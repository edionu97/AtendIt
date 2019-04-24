package application.notifications.nonStomp;

import application.notifications.nonStomp.models.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Custom websocket configuration
 * First message should be a hello message with username
 * If connection expires, should check connectivity periodically
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    TopicHandler handler(){
        return new TopicHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(
                handler(), "/topic"
        ).setAllowedOrigins("*");
    }

    /**
     * Topic handler class
     * Manage the connected usernameToSessions and broadcast message to users
     */
    private class TopicHandler extends TextWebSocketHandler {

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws  Exception{

            // get user message
            final Message message = parseMessage(
                    textMessage.getPayload(), Message.class
            );

            // hello message
            if(message.getSendTo() == null && message.getUserData() == null){
                //close previous session
                final WebSocketSession previousSession = usernameToSessions.get(message.getUserInfo().getUsern());
                if(previousSession != null && previousSession.isOpen()){
                    previousSession.close();
                }
                //save username alongside session
                usernameToSessions.put(
                        message.getUserInfo().getUsern(), session
                );
                return;
            }

            // broadcast data to users
            final List<String> inactive = new ArrayList<>();
            broadCast(
                    message.getSendTo(), message.getUserData(), inactive
            );
            inactive.forEach(closedSession -> usernameToSessions.remove(closedSession));
        }

        private <T> T parseMessage(final String received, final Class<T> toClass) throws  Exception {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(received, toClass);
        }

        /**
         * Send message to all users or to specific users
         * @param sendTo: list of username
         * @param userdata: data to be send
         */
        private void broadCast(final List<String> sendTo, final String userdata, final List<String> inactive){

            final TextMessage message = new TextMessage(userdata);

            // if list is empty => broadcast to all clients
            if(sendTo.isEmpty()){
                usernameToSessions.forEach((username, session)->{
                    try {
                        session.sendMessage(message);
                    }catch (Exception ex){
                        inactive.add(username);
                    }
                });
                return;
            }

            // send only to specific users
            sendTo.forEach(username -> {
                try{
                    final WebSocketSession session = usernameToSessions.get(username);
                    session.sendMessage(message);
                }catch (Exception ex){
                    inactive.add(username);
                }
            });
        }

        private ConcurrentHashMap<String, WebSocketSession> usernameToSessions = new ConcurrentHashMap<>();
    }
}
