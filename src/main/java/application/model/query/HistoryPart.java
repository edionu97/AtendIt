package application.model.query;

import application.model.History;

public class HistoryPart  extends History {
    public HistoryPart(final int historyId, final String grp, final String teacherName, final byte[] image) {
        super(grp, teacherName);
        super.setAttendanceImage(image);
        super.setHistoryId(historyId);
    }
}
