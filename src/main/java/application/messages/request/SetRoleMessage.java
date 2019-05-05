package application.messages.request;

import application.utils.model.UserRoles;

import java.io.Serializable;

public class SetRoleMessage implements Serializable {

    public SetRoleMessage() {
    }

    public SetRoleMessage(String username, UserRoles role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    private String username;

    private UserRoles role;

}
