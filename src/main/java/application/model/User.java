package application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import application.utils.model.UserRoles;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUserId() == user.getUserId() &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getPassword(), getUsername());
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @Column
    @JsonProperty("usern")
    private String username;

    @Column
    private UserRoles role;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private Profile profile;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    private Face face;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    private Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    private Set<Attendance> attendances = new HashSet<>();
}
