package messages.request;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class AuthenticationMessage implements Serializable {

    public AuthenticationMessage() {
    }

    public AuthenticationMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @JsonProperty("usern")
    private String username;

    @JsonProperty("passwd")
    private String password;
}
