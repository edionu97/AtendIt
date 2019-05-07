package application.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"userId", "courseId"}
                )
        }
)
public class Enrollment implements Serializable {

    public Enrollment() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment)) return false;
        Enrollment that = (Enrollment) o;
        return getEnrollmentId() == that.getEnrollmentId() &&
                Objects.equals(getGroup(), that.getGroup()) &&
                Objects.equals(getEnrollmentDate(), that.getEnrollmentDate()) &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getCourse(), that.getCourse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEnrollmentId(), getGroup(), getEnrollmentDate(), getUser(), getCourse());
    }

    public Enrollment(String group) {
        this.group = group;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int enrollmentId;

    @Column(name = "grupa")
    private String group;

    @Column
    @CreationTimestamp
    private Date enrollmentDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId")
    @Fetch(FetchMode.JOIN)
    private User user;

    @ManyToOne
    @JoinColumn(name = "courseId")
    @Fetch(FetchMode.JOIN)
    private Course course;
}
