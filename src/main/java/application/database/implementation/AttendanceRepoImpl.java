package application.database.implementation;

import application.database.AbstractRepoImpl;
import application.database.interfaces.IAttendanceRepo;
import application.model.Attendance;
import application.model.Course;
import application.model.History;
import application.model.User;
import application.model.query.AttendancePart;
import application.model.query.CoursePart;
import application.model.query.UserPart;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.ClassType;
import javafx.util.Pair;
import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jpa.spi.TupleBuilderTransformer;
import org.springframework.http.HttpStatus;
import scala.Int;

import javax.persistence.Entity;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AttendanceRepoImpl extends AbstractRepoImpl<Attendance> implements IAttendanceRepo {

    @Override
    public void addAttendance(
            final User student,
            final Course course,
            final History history,
            final byte[] image,
            final int height, final int width, final int type) throws ErrorMessageException {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();

            try {
                session.save(new Attendance(
                        student, course, history,
                        image, height, width, type
                ));
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new ErrorMessageException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public List<Attendance> getAllAttendancesFor(final User user) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Attendance> query = builder.createQuery(Attendance.class);
            Root<Attendance> table = query.from(Attendance.class);

            query.select(table).where(
                    builder.equal(table.get("user"), user)
            );

            query.orderBy(builder.desc(table.get("attendanceDate")));

            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public List<Attendance> getAll() {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Attendance> query = session.getCriteriaBuilder().createQuery(Attendance.class);

            Root<Attendance> table = query.from(Attendance.class);
            query.select(table);
            query.orderBy(builder.desc(table.get("attendanceDate")));

            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public Optional<Attendance> findById(int id) {

        Optional<Attendance> optionalAttendance = Optional.empty();

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();

            try {
                optionalAttendance = Optional.of(session.get(Attendance.class, id));
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }

        return optionalAttendance;
    }


    @Override
    public List<Attendance> getAttendancesForAt(final User user, final Course course) {
        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Attendance> query = builder.createQuery(Attendance.class);
            Root<Attendance> table = query.from(Attendance.class);


            query.select(table).where(
                    builder.and(
                            builder.equal(table.get("user"), user),
                            builder.equal(table.get("course"), course)
                    )
            );

            query.orderBy(builder.desc(table.get("attendanceDate")));

            return session.createQuery(query).getResultList();
        }
    }


    @Override
    public List<Attendance> getAttendancesForAt(
            final String teacherName, final String studentName, final String courseName, final ClassType classType) {


        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            //get attendance attendance that coressponds to the specific filter
            final String HQL =
                    "select new  application.model.query.AttendancePart(" +
                            "a.attendanceId, " +
                            "a.attendanceDate, " +
                            "c.courseId, " +
                            "teacher.userId" +
                            ") from Attendance a " +
                            "inner join a.course c " +
                            "inner join c.user teacher " +
                            "inner join a.user student " +
                            "where " +
                            "c.name = :courseName and " +
                            "c.type = :classType and " +
                            "student.username =:studentName " +
                            "and teacher.username =:teacherName " +
                            "order by a.attendanceDate desc ";
            final List<Attendance> attendances = session
                    .createQuery(HQL)
                    .setParameter("courseName", courseName)
                    .setParameter("classType", classType)
                    .setParameter("teacherName", teacherName)
                    .setParameter("studentName", studentName)
                    .getResultList();

            for (final Attendance attendancePart : attendances) {

                final int courseId = ((AttendancePart) attendancePart).getCourseId();
                final int userId = ((AttendancePart) attendancePart).getUserId();

                //get the course
                final String HQL_COURSE = "" +
                        "select new application.model.query.CoursePart(" +
                        "c.name, " +
                        "c.type, " +
                        "c.abbreviation, " +
                        "c.courseId" +
                        ") " +
                        "from Course c where c.courseId =:courseId";
                final CoursePart course = session
                        .createQuery(HQL_COURSE, CoursePart.class)
                        .setParameter("courseId", courseId)
                        .getSingleResult();

                //get the profile
                final String HQL_PROFILE =
                        "select new application.model.query.UserPart(" +
                                "u.username, " +
                                "u.role," +
                                "u.userId" +
                                ")" +
                                "from User u left join u.profile p where u.username = :teacherName";
                final UserPart user = session
                        .createQuery(HQL_PROFILE, UserPart.class)
                        .setParameter("teacherName", teacherName)
                        .getSingleResult();

                course.setUser(user);
                attendancePart.setCourse(course);
            }

            return attendances;
        }

    }

}
