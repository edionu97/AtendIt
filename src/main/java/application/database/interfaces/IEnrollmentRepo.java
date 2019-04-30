package application.database.interfaces;

import application.model.Course;
import application.model.Enrollment;
import application.model.User;
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
            final User student, final Course course, final String group) throws  Exception;

    /*
        Delete the enrollment
     */
    void delete(final Enrollment enrollment) throws  Exception;

    /**
     * Update the enrollment
     * @param enrollment: the enrollment that contains new or same info (update based on enrollment id)
     */
    void update(Enrollment enrollment);

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

}