package application.messages.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DeleteAttendanceMessage implements Serializable {

    public DeleteAttendanceMessage() {
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    @JsonProperty(value = "id")
    private int attendanceId;
}
