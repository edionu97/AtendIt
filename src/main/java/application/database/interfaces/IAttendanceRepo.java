package application.database.interfaces;

import application.model.Attendance;
import application.model.Course;
import application.model.User;

import java.util.List;
import java.util.Optional;

public interface IAttendanceRepo {

    /**
     * Add a new attendance to user
     * @param student: student to whom attendance will be given
     * @param course: the course at attendance is given
     * @throws Exception if something is wrong
     */
    void addAttendance(final User student, final Course course) throws  Exception;

    /**
     * Update the attendance
     */
    void update(final  Attendance attendance);

    /**
     * Delete the attendance
     */
    void delete(final  Attendance attendance) throws  Exception;

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
    List<Attendance> getAttendancesForAt(final User user, final Course course);


}