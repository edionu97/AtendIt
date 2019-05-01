package application.messages.request;

import application.utils.model.ClassType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GetAttendancesForAtMessage implements Serializable {

    public GetAttendancesForAtMessage() {
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    @JsonProperty(value = "student")
    private String studentName;

    @JsonProperty(value = "courseType")
    private ClassType type;

    @JsonProperty(value = "teacher")
    private String teacherName;

    private String courseName;
}
