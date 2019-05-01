package application.model;


import application.utils.model.ClassType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"name", "type", "userId"}
                )
        }
)
public class Course implements Serializable {

    public Course() {
    }

    public Course(String name, ClassType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return getCourseId() == course.getCourseId() &&
                Objects.equals(getName(), course.getName()) &&
                getType() == course.getType() && user.equals(course.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourseId(), getName(), getType());
    }

    public int getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public ClassType getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(ClassType type) {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        enrollments.forEach(enrollment -> enrollment.setCourse(this));
        this.enrollments = enrollments;
    }

    public Set<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(Set<Attendance> attendances) {
        attendances.forEach(attendance -> attendance.setCourse(this));
        this.attendances = attendances;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseId;

    @Column
    private String name;

    @Column
    private ClassType type;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Attendance> attendances = new HashSet<>();
}
