package application.messages.request;

import application.utils.model.ClassType;

import java.io.Serializable;

public class AbsentsMessage implements Serializable {

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public ClassType getCourseType() {
        return courseType;
    }

    public void setCourseType(ClassType courseType) {
        this.courseType = courseType;
    }

    public String getGrupa() {
        return grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    private String teacherName;
    private String courseName;
    private ClassType courseType;
    private String grupa;
    private int historyId;
}
