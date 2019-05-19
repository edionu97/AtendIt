package application.model.query;

import application.model.Attendance;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class AttendancePart extends Attendance {

    public  AttendancePart(final int attendanceId, final  Date attendanceDate, final int courseId, final int userId){
        setAttendanceId(attendanceId);
        setAttendanceDate(attendanceDate);
        this.courseId = courseId;
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getUserId() {
        return userId;
    }

    @JsonIgnore
    private int userId;

    @JsonIgnore
    private int courseId;
}
