package application.database.interfaces;

import application.model.Attendance;
import application.model.Course;
import application.model.History;
import application.model.User;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.ClassType;

import java.util.List;
import java.util.Optional;

public interface IAttendanceRepo {

    /**
     * Add a new attendance to user
     *
     * @param student: student to whom attendance will be given
     * @param course:  the course at attendance is given
     * @throws ErrorMessageException if something is wrong
     */
    void addAttendance(
            final User student,
            final Course course,
            final History history) throws ErrorMessageException;

    /**
     * Update the attendance
     */
    void update(final Attendance attendance) throws ErrorMessageException;

    /**
     * Delete the attendance
     */
    void delete(final Attendance attendance) throws ErrorMessageException;

    /**
     * Returns a list with all the attendances for a specific user
     */
    List<Attendance> getAllAttendancesFor(final User user);

    /**
     * Returns all attendances
     */
    List<Attendance> getAll();

    /**
     * @param id: the attendance's id
     * @return an attendance based on its id
     */
    Optional<Attendance> findById(final int id);

    /**
     * Return all user attendances at a specific course
     */
    @Deprecated
    List<Attendance> getAttendancesForAt(final User user, final Course course);


    List<Attendance> getAttendancesForAt(
            final String teacherName, final String studentName, final String courseName, final ClassType classType);

}
