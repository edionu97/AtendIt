package application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class History implements Serializable {

    public History(final String grp, final String teacherName) {
        this.grp = grp;
        this.teacherName = teacherName;
    }

    public History() {
    }

    public int getHistoryId() {
        return historyId;
    }

    public String getGrp() {
        return grp;
    }

    public void setGrp(String grp) {
        this.grp = grp;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public Set<Attendance> getAttendances() {
        return attendances;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setAttendances(final Set<Attendance> attendances) {
        attendances.forEach(attendance -> attendance.setHistory(this));
        this.attendances = attendances;
    }

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int historyId;

    @Column
    private String grp;

    @Column
    private String teacherName;

    @JsonIgnore
    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<Attendance> attendances = new HashSet<>();
}
