package application.messages.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ChangePasswordMessage  implements Serializable {

    public ChangePasswordMessage(String username, String password, String newPassword) {
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
    }

    public ChangePasswordMessage() {
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @JsonProperty("new")
    private String newPassword;
}
