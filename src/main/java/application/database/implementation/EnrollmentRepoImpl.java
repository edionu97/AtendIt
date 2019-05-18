package application.database.implementation;

import application.database.AbstractRepoImpl;
import application.database.interfaces.IEnrollmentRepo;
import application.model.Course;
import application.model.Enrollment;
import application.model.User;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.ClassType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import scala.Int;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentRepoImpl extends AbstractRepoImpl<Enrollment> implements IEnrollmentRepo {

    @Override
    public void addEnrollment(final User student, final Course course, final String group) throws ErrorMessageException {

        final Enrollment enrollment = new Enrollment(group);
        enrollment.setCourse(course);
        enrollment.setUser(student);

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();

            try {
                session.save(enrollment);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new ErrorMessageException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    }

    @Override
    public List<Enrollment> getEnrollmentsFor(String studentName) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Enrollment> query = session.getCriteriaBuilder().createQuery(Enrollment.class);

            Root<Enrollment> table = query.from(Enrollment.class);

            query.select(table).where(
                    builder.equal(
                            table.get("user").get("username"), studentName)
            );

            query.select(table);

            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public Optional<Enrollment> findEnrollmentByUserAndCourse(final User user, final Course course) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Enrollment> query = builder.createQuery(Enrollment.class);
            Root<Enrollment> table = query.from(Enrollment.class);

            query.select(table).where(
                    builder.and(
                            builder.equal(
                                    table.get("user"), user
                            ),
                            builder.equal(
                                    table.get("course"), course
                            )
                    )
            );

            return Optional.ofNullable(session.createQuery(query).getSingleResult());
        } catch (NoResultException ignored) {
        }

        return Optional.empty();
    }

    @Override
    public List<String> getClassesEnrolledAtTeachersCourses(String teacherName) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            final String HQL =
                    "select distinct e.group from Enrollment e " +
                            "inner join e.course c " +
                            "inner join c.user u " +
                    "where u.username = :username";

            return session
                    .createQuery(HQL)
                    .setParameter("username", teacherName)
                    .getResultList();

        } catch (NoResultException ignored) {
        }

        return new ArrayList<>();
    }

    @Override
    public List<ClassType> getEnrollmentAtAllClassTypesFor(
            final String studentName, final String courseName, final String teacherName) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            final String HQL =
                    "select course.type from Enrollment e " +
                        "inner join e.course course " +
                        "inner join course.user teacher " +
                        "inner join e.user student " +
                    "where student.username = :studentName and course.name = :courseName and teacher.username =:teacherName";

            return session
                    .createQuery(HQL)
                    .setParameter("studentName", studentName)
                    .setParameter("courseName", courseName)
                    .setParameter("teacherName", teacherName)
                    .getResultList();
        } catch (NoResultException ignored) {
        }

        return new ArrayList<>();
    }

    @Override
    public boolean isEnrolledAtCourse(
            final String studentName, final String courseName, final ClassType type, final String teacherName) {

        try (Session session = persistenceUtils.getSessionFactory().openSession()) {

            final String HQL =
                    "select student.role from Enrollment e " +
                        "inner join e.course c " +
                        "inner join e.user student " +
                        "inner join c.user teacher " +
                    "where c.name = :courseName and c.type = :courseType and student.username = :studentName and teacher.username = :teacherName";


            return !session
                    .createQuery(HQL)
                    .setParameter("courseName", courseName)
                    .setParameter("courseType", type)
                    .setParameter("studentName", studentName)
                    .setParameter("teacherName",teacherName).getResultList().isEmpty();

        } catch (NoResultException ignored) {
        }

        return false;
    }


}
