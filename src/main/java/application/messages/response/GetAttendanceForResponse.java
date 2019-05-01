package application.messages.response;

import application.model.Attendance;

import java.io.Serializable;
import java.util.List;

public class GetAttendanceForResponse implements Serializable {

    public GetAttendanceForResponse(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public GetAttendanceForResponse() {
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    private List<Attendance> attendances;
}
