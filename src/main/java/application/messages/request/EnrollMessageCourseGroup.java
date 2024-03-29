package application.messages.request;

import application.utils.model.ClassType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class EnrollMessageCourseGroup implements Serializable {

    public long getGroup() {
        return group;
    }

    public void setGroup(long group) {
        this.group = group;
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

    @JsonProperty(value = "courseType")
    private ClassType type;

    @JsonProperty(value = "teacher")
    private String teacherName;

    private String courseName;

    @JsonProperty(value = "cls")
    private long group;
}
