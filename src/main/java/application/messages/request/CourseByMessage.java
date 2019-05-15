package application.messages.request;

import application.utils.model.ClassType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CourseByMessage implements Serializable {

    public CourseByMessage(){

    }

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassType getType() {
        return type;
    }

    public void setType(ClassType type) {
        this.type = type;
    }

    @JsonProperty("teacher")
    private String username;

    @JsonProperty("courseName")
    private String name;

    @JsonProperty("courseType")
    private ClassType type;

    @JsonProperty("abr")
    private String abreviation;
}
