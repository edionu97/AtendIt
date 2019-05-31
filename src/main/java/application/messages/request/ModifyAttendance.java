package application.messages.request;

import application.utils.model.ClassType;

import java.io.Serializable;
import java.util.List;

public class ModifyAttendance implements Serializable {

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(final String courseName) {
        this.courseName = courseName;
    }

    public ClassType getType() {
        return type;
    }

    public void setType(final ClassType type) {
        this.type = type;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(final String teacherName) {
        this.teacherName = teacherName;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(final int historyId) {
        this.historyId = historyId;
    }

    public List<String> getPresents() {
        return presents;
    }

    public void setPresents(final List<String> presents) {
        this.presents = presents;
    }

    private int historyId;
    private ClassType type;
    private String courseName;
    private String teacherName;
    private List<String> presents;
}
