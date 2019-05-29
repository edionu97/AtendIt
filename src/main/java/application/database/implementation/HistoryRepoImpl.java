package application.database.implementation;

import application.database.AbstractRepoImpl;
import application.database.interfaces.IHistoryRepo;
import application.model.History;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HistoryRepoImpl extends AbstractRepoImpl<History> implements IHistoryRepo {
    @Override
    public List<History> getAllFor(final String teacher) {

        try (final Session session = persistenceUtils.getSessionFactory().openSession()) {

            final String HQL =
                    "select  new application.model.query.HistoryAttendanceCourseStudentPart(" +
                        "h.grp, " +
                        "h.teacherName, " +
                        "a.attendanceDate, " +
                        "h.attendanceImage, " +
                        "c.name, " +
                        "c.abbreviation, " +
                        "c.type, " +
                        "student.username, " +
                        "student.role)" +
                    "from History h " +
                            "inner join h.attendances a " +
                            "inner join a.course c " +
                            "inner join a.user student " +
                    "where h.teacherName=:teacher";

            return session.createQuery(HQL).setParameter("teacher", teacher).getResultList();
        }
    }

    @Override
    public Optional<History> findHistoryBy(final String teacher, final String group) {

        try (final Session session = persistenceUtils.getSessionFactory().openSession()) {

            final String HQL =
                    "select new application.model.query.HistoryPart(" +
                            "h.historyId, " +
                            "h.grp, " +
                            "h.teacherName) " +
                            "from History h where h.teacherName=:teacher and h.grp=:class";

            return session
                    .createQuery(HQL)
                    .setParameter("teacher", teacher)
                    .setParameter("class", group)
                    .getResultList()
                    .stream()
                    .findFirst();
        }
    }
}
