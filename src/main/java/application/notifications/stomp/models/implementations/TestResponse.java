package application.notifications.stomp.models.implementations;

import application.notifications.stomp.models.IResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestResponse implements IResponse {

    public TestResponse(String testResponse) {
        this.testResponse = testResponse;
    }


    public TestResponse() {
    }

    public String getTestResponse() {
        return testResponse;
    }

    public void setTestResponse(String testResponse) {
        this.testResponse = testResponse;
    }

    @JsonProperty("response")
    private String testResponse;
}
