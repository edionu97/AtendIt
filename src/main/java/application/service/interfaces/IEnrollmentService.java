package application.service.interfaces;

import application.model.Enrollment;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.ClassType;

import java.util.List;
import java.util.Map;

public interface IEnrollmentService {

    /**
     * Add new enrollment to student @param studentName at course [
     *
     * @param courseName: course name
     * @param type:       course type
     * @param teacherName ]
     * @throws Exception if something went wrong
     */
    void addEnrollment(
            final String studentName, final String courseName, final ClassType type, final String teacherName, final String group) throws ErrorMessageException;

    /**
     * Delete the student's enrollment at course [
     *
     * @param courseName: course name
     * @param type:       course type
     * @param teacherName ]
     * @throws Exception if something went wrong
     */
    void deleteEnrollment(
            final String studentName, final String courseName, final ClassType type, final String teacherName) throws ErrorMessageException;


    /**
     * Returns true if student is enrolled at course
     */
    boolean isEnrolledAtCourse(final String studentName, final String courseName, final ClassType type, final String teacherName);

    /**
     * Returns all student enrollments
     *
     * @param studentName: the name of the student
     * @return all enrollments for a specific student
     */
    Map<String, List<Enrollment>> getEnrollmentsFor(final String studentName);

    Map<String, Boolean> getEnrollAtWholeCourseType(final String studentName, final  String teacherName, final String courseName);

    List<String> getClassesEnrolledAtTeacherCourses(final String teacherName);
}
