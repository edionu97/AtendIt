package application.messages.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GetAttendanceForMessage implements Serializable {

    public GetAttendanceForMessage() {
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
