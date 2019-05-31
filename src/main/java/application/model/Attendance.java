package application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
public class Attendance implements Serializable {

    public Attendance() {
    }

    public Attendance(
            final User user,
            final Course course,
            final History history) {
        this.user = user;
        this.course = course;
        this.history = history;
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

    public History getHistory() {
        return history;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public void setHistory(final History history) {
        this.history = history;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attendanceId;

    @Column
    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date attendanceDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId")
    @Fetch(value = FetchMode.JOIN)
    private User user;

    @ManyToOne
    @JoinColumn(name = "courseId")
    @Fetch(value = FetchMode.JOIN)
    private Course course;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "historyId")
    private History history;
}
