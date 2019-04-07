package application.messages.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CreateUpdateMessage implements Serializable {

    public CreateUpdateMessage(String email, String firstName, String lastName, String phoneNumber, String username) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.username = username;
    }

    public CreateUpdateMessage() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    private String email;

    @JsonProperty(value = "first")
    private String firstName;

    @JsonProperty(value = "last")
    private String lastName;

    @JsonProperty(value = "phone")
    private String phoneNumber;

    @JsonProperty(value = "usern")
    private String username;
}
