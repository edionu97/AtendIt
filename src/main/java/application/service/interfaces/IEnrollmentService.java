package application.service.interfaces;

import application.model.Enrollment;
import application.utils.model.ClassType;

import java.util.List;

public interface IEnrollmentService {

    /**
     * Add new enrollment to student @param studentName at course [
         * @param courseName: course name
         * @param type: course type
         * @param teacherName
     * ]
     * @throws Exception if something went wrong
     */
    void addEnrollment(
            final String studentName, final String courseName, final ClassType type, final String teacherName, final String group) throws  Exception;

    /**
     * Delete the student's enrollment at course [
     * @param courseName: course name
     * @param type: course type
     * @param teacherName
     * ]
     * @throws Exception if something went wrong
     */
    void deleteEnrollment(
            final String studentName, final String courseName, final ClassType type, final String teacherName) throws  Exception;


    /**
     * Returns all student enrollments
     * @param studentName: the name of the student
     * @return a list of values
     */
    List<Enrollment> getEnrollmentsFor(final String studentName);

}
