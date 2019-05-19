package application.service.interfaces;


import application.model.Attendance;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.ClassType;

import java.util.List;
import java.util.Optional;

public interface IAttendanceService {

    /**
     * Add new attendance to a specific student at a specific course
     *
     * @param studentName: the name of the student
     * @param courseName:  the name of the course
     * @param type:        the course's type
     * @param teacherName: the name of the teacher who posted the course
     * @throws application.utils.exceptions.ErrorMessageException: if something went wrong
     */
    void addAttendance(
            final String studentName, final String courseName, final ClassType type, final String teacherName) throws ErrorMessageException;

    /**
     * Returns true if user has a list with face images and false contrary
     *
     * @param username: user's username
     */
    boolean hasFaceSet(final String username);

    /**
     * Deletes the attendance with specified id
     *
     * @param attendanceId: the id
     * @throws application.utils.exceptions.ErrorMessageException: if something went wrong
     */
    void delete(final int attendanceId) throws ErrorMessageException;

    /**
     * Update a specific attendance
     *
     * @param attendance: the attendance that will be updated
     */
    void update(final Attendance attendance) throws ErrorMessageException;

    /**
     * @param id: the attendance id
     * @return optional.of attendance if the attendance with specified id exist, otherwise optional.empty
     */
    Optional<Attendance> findById(final int id);

    /**
     * @return a  list with all attendances from database
     */
    List<Attendance> getAll();

    /**
     * @param username: the student's username
     * @return a list with all attendances for a specific student
     */
    List<Attendance> getAttendanceFor(final String username);

    /**
     * @param username:    the student username
     * @param courseName:  the course name
     * @param type:        the course type
     * @param teacherName: the name of the student
     * @return get attendances for a student at a specific couse
     */
    List<Attendance> getAttendanceForAt(
            final String username, final String courseName, final ClassType type, final String teacherName);

    void automaticAttendance(
            final byte[] attendanceVideo, final String teacherName, final String attendanceClass) throws  ErrorMessageException;
}
