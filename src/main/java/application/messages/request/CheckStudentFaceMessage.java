package application.messages.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CheckStudentFaceMessage implements Serializable {

    public CheckStudentFaceMessage(String username) {
        this.username = username;
    }

    public CheckStudentFaceMessage() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty(value = "usern")
    private String username;
}
