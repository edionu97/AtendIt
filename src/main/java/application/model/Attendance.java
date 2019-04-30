package application.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
public class Attendance implements Serializable {

    public Attendance() {
    }

    public Attendance(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attendance)) return false;
        Attendance that = (Attendance) o;
        return getAttendanceId() == that.getAttendanceId() &&
                Objects.equals(getAttendanceDate(), that.getAttendanceDate()) &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getCourse(), that.getCourse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAttendanceId(), getAttendanceDate(), getUser(), getCourse());
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public User getUser() {
        return user;
    }

    public Course getCourse() {
        return course;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attendanceId;

    @Column
    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date attendanceDate;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

}
