package application.notifications.nonStomp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {

    public Message(UserInfo userInfo, String userData, List<String> sendTo) {
        this.userInfo = userInfo;
        this.userData = userData;
        this.sendTo = sendTo;
    }

    public Message() {
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public List<String> getSendTo() {
        return sendTo;
    }

    public void setSendTo(List<String> sendTo) {
        this.sendTo = sendTo;
    }

    @JsonProperty("userInfo")
    private UserInfo userInfo;

    @JsonProperty("userData")
    private String userData;


    public class UserInfo implements Serializable{

        public UserInfo(String usern, WebSocketSession session) {
            this.usern = usern;
            this.session = session;
        }

        public UserInfo() {
        }

        public String getUsern() {
            return usern;
        }

        public void setUsern(String usern) {
            this.usern = usern;
        }

        public WebSocketSession getSession() {
            return session;
        }

        public void setSession(WebSocketSession session) {
            this.session = session;
        }

        @JsonProperty("usern")
        private String usern;

        @JsonIgnore
        private WebSocketSession session;
    }


    @JsonProperty("sendTo")
    private List<String> sendTo;
}
