package application.database.interfaces;

import application.model.Course;

import java.util.List;

public interface ICourseRepo {

    /**
     * Get all courses from database
     * return: a list of courses
     */
    List<Course> getAll();
}
