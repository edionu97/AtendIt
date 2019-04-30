package application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import application.utils.model.UserRoles;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
        this.profile.setUser(this);
    }

    public Face getFace() {
        return face;
    }

    public void setFace(Face face) {
        this.face = face;
        this.face.setUser(this);
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        courses.forEach(course -> course.setUser(this));
        this.courses = courses;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        enrollments.forEach(enrollment -> enrollment.setUser(this));
        this.enrollments = enrollments;
    }

    public Set<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(Set<Attendance> attendances) {
        attendances.forEach(attendance -> attendance.setUser(this));
        this.attendances = attendances;
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

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Profile profile;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Face face;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Attendance> attendances = new HashSet<>();
}
