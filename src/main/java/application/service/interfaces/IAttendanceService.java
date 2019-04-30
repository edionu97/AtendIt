package application.service.interfaces;


import application.model.Attendance;
import application.utils.model.ClassType;

import java.util.List;
import java.util.Optional;

public interface IAttendanceService {

        void addAttendance(
                final String studentName, final String courseName, final ClassType type, final String teacherName )throws  Exception;

        void delete(final int attendanceId) throws Exception;

        void update(final Attendance attendance);

        Optional<Attendance> findById(final int id);

        List<Attendance> getAll();

        List<Attendance> getAttendanceFor(final String username);

        List<Attendance> getAttendanceForAt(final String username, final String courseName, final ClassType type, final String teacherName);
}
