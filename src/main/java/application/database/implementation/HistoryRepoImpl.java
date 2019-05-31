package application.database.implementation;

import application.database.AbstractRepoImpl;
import application.database.interfaces.IHistoryRepo;
import application.model.History;
import application.model.User;
import application.utils.model.ClassType;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HistoryRepoImpl extends AbstractRepoImpl<History> implements IHistoryRepo {

    @Override
    public Optional<History> findHisoryById(int historyId) {
        try (final Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<History> query = builder.createQuery(History.class);
            Root<History> table = query.from(History.class);

            query.select(table).where(
                    builder.equal(
                            table.get("historyId"),
                            historyId
                    )
            );

            return session.createQuery(query).getResultList().stream().findFirst();
        }
    }

    @Override
    public List<History> getAllFor(final String teacher) {

        try (final Session session = persistenceUtils.getSessionFactory().openSession()) {

            final String HQL =
                    "select new application.model.query.HistoryPart(" +
                            "h.historyId, " +
                            "h.grp, " +
                            "h.teacherName, " +
                            "h.attendanceImage) " +
                            "from History h";

            return session.createQuery(HQL).getResultList();
        }
    }

    @Override
    public List<History> getAllForAt(final String teacher, final int historyId) {

        try (final Session session = persistenceUtils.getSessionFactory().openSession()) {

            final String HQL =
                    "select  new application.model.query.HistoryAttendanceCourseStudentPart(" +
                            "h.historyId, " +
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
                            "where h.teacherName=:teacher and h.historyId =:historyId ";

            return session
                    .createQuery(HQL)
                    .setParameter("teacher", teacher)
                    .setParameter("historyId", historyId)
                    .getResultList();
        }
    }

    @Override
    public Optional<History> findHistoryBy(final String teacher, final String group) {
        try (final Session session = persistenceUtils.getSessionFactory().openSession()) {

            final String HQL =
                    "select new application.model.query.HistoryPart(" +
                            "h.historyId, " +
                            "h.grp, " +
                            "h.teacherName, " +
                            "h.attendanceImage) " +
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

    @Override
    public List<User> getAbsentUsers(
            final String teacherName, final String courseName, final ClassType courseType, final String grupa, final int historyId) {

        try (final Session session = persistenceUtils.getSessionFactory().openSession()) {

            //get enrolled students
            final String HQL =
                    "select new application.model.query.UserPart(" +
                            "student.username, " +
                            "student.role, " +
                            "student.userId) from Enrollment e " +
                            "inner join e.course c " +
                            "inner join e.user student " +
                            "inner join c.user teacher " +
                            "where c.name =:courseName and c.type=:courseType and teacher.username=:teacherName and e.group=:grupa";
            final List<User> absents = session
                    .createQuery(HQL)
                    .setParameter("courseType", courseType)
                    .setParameter("courseName", courseName)
                    .setParameter("teacherName", teacherName)
                    .setParameter("grupa", grupa).getResultList();

            //get present students
            final String HQL2 =
                    "select a.user.userId from History h " +
                            "inner join h.attendances a " +
                            "where a.history.historyId = :historyId";
            final List<Integer> presentStudents = session
                    .createQuery(HQL2)
                    .setParameter("historyId", historyId)
                    .getResultList();

            //remove present users
            presentStudents.forEach(x -> {
                absents.removeIf(y -> y.getUserId() == x);
            });

            return absents;
        }
    }
}
