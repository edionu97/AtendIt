package application.messages.request;

import application.utils.model.ClassType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class AddEnrollmentMessage implements Serializable {

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public ClassType getType() {
        return type;
    }

    public void setType(ClassType type) {
        this.type = type;
    }

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @JsonProperty(value = "student")
    private String studentName;

    @JsonProperty(value = "courseType")
    private ClassType type;

    @JsonProperty(value = "teacher")
    private String teacherName;

    private String courseName;

    private String group;
}

