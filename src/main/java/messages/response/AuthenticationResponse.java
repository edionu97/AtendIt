package messages.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @JsonProperty("status")
    private HttpStatus status;

    @JsonProperty("msg")
    private String message;
}
