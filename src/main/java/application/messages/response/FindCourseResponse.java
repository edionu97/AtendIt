package application.messages.response;

import application.model.Course;

import java.io.Serializable;

public class FindCourseResponse implements Serializable {

    public FindCourseResponse(final Course course){
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    private Course course;
}
