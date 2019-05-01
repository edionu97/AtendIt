package application.messages.response;

import application.model.Enrollment;

import java.io.Serializable;
import java.util.List;

public class GetEnrollmentsResponse implements Serializable {

    public GetEnrollmentsResponse() {
    }

    public GetEnrollmentsResponse(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    private List<Enrollment> enrollments;
}
