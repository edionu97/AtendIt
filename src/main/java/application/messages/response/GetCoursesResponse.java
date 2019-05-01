package application.messages.response;

import application.model.Course;

import java.io.Serializable;
import java.util.List;

public class GetCoursesResponse implements Serializable {

    public GetCoursesResponse(List<Course> courses) {
        this.courses = courses;
    }

    public GetCoursesResponse(){

    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    private List<Course> courses;
}
