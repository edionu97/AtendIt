package application.model.query;

import application.model.History;

public class HistoryPart  extends History {
    public HistoryPart(final int historyId, final String grp, final String teacherName) {
        super(grp, teacherName);
        super.setHistoryId(historyId);
    }
}
