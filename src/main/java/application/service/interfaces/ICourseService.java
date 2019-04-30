package application.service.interfaces;

import application.model.Course;
import application.utils.model.ClassType;

import java.util.List;
import java.util.Optional;

public interface ICourseService {

    /**
     * Add a new course into database
     * If in database exists a course with same (name, type, userId) than the new value will not be saved
     * @param username: teacher's username
     * @param courseName: name of the course
     * @param type: course type
     * @throws Exception: if the action could not be completed (not teacher or already or in database does not exist an user with given username)
     */
    void addCourse(
            final String username, final String courseName, final ClassType type) throws Exception;


    /**
     * Returns a list with all courses proposed by user with username = @param username
     */
    List<Course> getCoursesFor(final String username);

    /**
        Return a specific user course
     */
    Optional<Course> findCourseBy(
            final String username, final String name, final ClassType type);

}
