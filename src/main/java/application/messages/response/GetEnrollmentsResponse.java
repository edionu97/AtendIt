package application.messages.response;

import application.model.Enrollment;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GetEnrollmentsResponse implements Serializable {

    public GetEnrollmentsResponse() {
    }

    public Map<String, List<Enrollment>> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Map<String, List<Enrollment>> enrollments) {
        this.enrollments = enrollments;
    }

    public GetEnrollmentsResponse(Map<String, List<Enrollment>> enrollments) {
        this.enrollments = enrollments;
    }

    private Map<String, List<Enrollment>> enrollments;
}
