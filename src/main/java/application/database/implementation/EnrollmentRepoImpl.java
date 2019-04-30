package application.database.implementation;

import application.database.AbstractRepoImpl;
import application.database.interfaces.IEnrollmentRepo;
import application.model.Course;
import application.model.Enrollment;
import application.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class EnrollmentRepoImpl extends AbstractRepoImpl<Enrollment> implements IEnrollmentRepo {

    @Override
    public void addEnrollment(final User student, final Course course, final String group) throws Exception {

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
                throw new Exception(e.getMessage());
            }
        }

    }

    @Override
    public List<Enrollment> getEnrollmentsFor(String studentName) {

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

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
}
