package application.model.query;

import application.model.History;
import application.model.Profile;
import application.utils.model.ClassType;
import application.utils.model.UserRoles;

import java.util.Date;

public class HistoryAttendanceCourseStudentPart extends History {

    public HistoryAttendanceCourseStudentPart(
            final int historyId,
            final String grp,
            final String teacherName,
            final Date attendanceDate,
            final byte[] attendanceImage,
            final String courseName,
            final String courseAbr,
            final ClassType courseType,
            final String studentName,
            final UserRoles role) {

        super(grp, teacherName);
        super.setHistoryId(historyId);
        this.attendanceDate = attendanceDate;
        this.attendanceImage = attendanceImage;
        this.courseName = courseName;
        this.courseType = courseType;
        this.courseAbr = courseAbr;
        this.studentName = studentName;
        this.role = role;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public byte[] getAttendanceImage() {
        return attendanceImage;
    }

    public void setAttendanceImage(byte[] attendanceImage) {
        this.attendanceImage = attendanceImage;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public ClassType getCourseType() {
        return courseType;
    }

    public void setCourseType(ClassType courseType) {
        this.courseType = courseType;
    }

    public String getCourseAbr() {
        return courseAbr;
    }

    public void setCourseAbr(String courseAbr) {
        this.courseAbr = courseAbr;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public Profile getStudentProfile() {
        return studentProfile;
    }

    public void setStudentProfile(Profile studentProfile) {
        this.studentProfile = studentProfile;
    }

    //from attendance
    private Date attendanceDate;
    private byte[] attendanceImage;

    //from course
    private String courseName;
    private ClassType courseType;
    private String courseAbr;

    //from user
    private String studentName;
    private UserRoles role;

    private Profile studentProfile;
}
