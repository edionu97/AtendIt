package application.model.query;

import application.model.Course;
import application.model.User;
import application.utils.model.ClassType;

public class CoursePart extends Course {

    public CoursePart(
            final String name, final ClassType type, final String abbreviation, final int courseId){

        super(name, type, abbreviation);

        setCourseId(courseId);
    }
}
