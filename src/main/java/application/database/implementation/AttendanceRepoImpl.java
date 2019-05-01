package application.database.implementation;

import application.database.AbstractRepoImpl;
import application.database.interfaces.IAttendanceRepo;
import application.model.Attendance;
import application.model.Course;
import application.model.User;
import application.utils.exceptions.ErrorMessageException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class AttendanceRepoImpl extends AbstractRepoImpl<Attendance> implements IAttendanceRepo {

    @Override
    public void addAttendance(final User student, final Course course) throws ErrorMessageException {

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            Transaction transaction = session.beginTransaction();

            try{
                session.save(new Attendance(
                        student, course
                ));
                transaction.commit();
            }catch (Exception e){
                transaction.rollback();
                throw  new ErrorMessageException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public List<Attendance> getAllAttendancesFor(final User user) {

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

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

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

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

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            Transaction transaction = session.beginTransaction();

            try{
                optionalAttendance = Optional.of(session.get(Attendance.class, id));
                transaction.commit();
            }catch (Exception e){
                transaction.rollback();
            }
        }

        return optionalAttendance;
    }


    @Override
    public List<Attendance> getAttendancesForAt(final User user, final Course course) {
        try(Session session = persistenceUtils.getSessionFactory().openSession()){

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

}
