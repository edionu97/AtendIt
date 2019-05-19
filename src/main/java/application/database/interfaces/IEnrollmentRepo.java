package application.database.interfaces;

import application.model.Course;
import application.model.Enrollment;
import application.model.User;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.ClassType;

import java.util.List;
import java.util.Optional;

public interface IEnrollmentRepo {

    /**
     * Add new enrollment into database
     * @param student: the student that enrolls to course
     * @param course: the course student enrolls at
     * @param group: the student's group name
     * @throws Exception: if something went wrong
     */
    void addEnrollment(
            final User student, final Course course, final String group) throws  ErrorMessageException;

    /*
        Delete the enrollment
     */
    void delete(final Enrollment enrollment) throws  ErrorMessageException;

    /**
     * Update the enrollment
     * @param enrollment: the enrollment that contains new or same info (update based on enrollment id)
     */
    void update(Enrollment enrollment) throws ErrorMessageException;

    /**
     *
     * @param studentName: the student name(username)
     * @return a list with all student's enrollments
     */
    List<Enrollment> getEnrollmentsFor(final String studentName);

    /**
     * @return that enrollment which belongs to @param user at course @param course
     */
    Optional<Enrollment> findEnrollmentByUserAndCourse(final User user, final Course course);

    /**
     *
     * @param teacherName: the name of the teacher
     * @return a list of strings representing
         * all the student's grupes that are enrolled to at least one course posted by teacher
         * with name @param teacherName
     */
    List<String> getClassesEnrolledAtTeachersCourses(final String teacherName);

    /**
     * @param studentName: the name of the student
     * @param courseName: the name of the course
     * @param teacherName: the name of the teacher
     * @return a list with all enrollments for a specific user to a specific course
     */
    List<ClassType> getEnrollmentAtAllClassTypesFor(
            final String studentName, final String courseName, final String teacherName);

    /**
     *
     * @param studentName: the name of the student
     * @param courseName: the name of te course
     * @param type: the course type
     * @param teacherName: the name of the teacher that proposed the course
     * @return true if student is enrolled at course or false otherwise
     */
    boolean isEnrolledAtCourse(
            final String studentName, final String courseName, final ClassType type, final String teacherName);

    /**
     * @param teacherName: teacher username
     * @param courseName: course name
     * @param classType: the student's class type
     * @param grup: the student's grup
     * @return the number of students enrolled at a specific course
     */
    long getEnrolledNumberAtCourseFromClass(
            final String teacherName, final String courseName, final ClassType classType, final long grup);
}
