package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import utils.model.UserRoles;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"username"}
        )
)
public class User implements Serializable {

    /**
     * Creates a default user
     * Password and username are null
     */
    public User() {
    }

    /**
     * Creates a user
     * @param password:  string representing the password of the user
     * @param username:  string representing the user's username
     * @param role:  enum represents the user's role
     */
    public User(String username, String password, UserRoles role) {
        this.password = password;
        this.username = username;
        this.role = role;
    }

    /**
     * Creates an admin user
     * @param password: string representing user's password
     * @param username: string representing user's username
     */
    public User(String username, String password) {
        this.password = password;
        this.username = username;
        this.role = UserRoles.TEACHER;
    }

    public int getUserId(){
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int userId;

    @Column
    @JsonProperty("passwd")
    private String password;

    @Column
    @JsonProperty("usern")
    private String username;

    @Column
    private UserRoles role;
}
