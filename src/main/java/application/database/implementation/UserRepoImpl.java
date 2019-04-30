package application.database.implementation;

import application.database.AbstractRepoImpl;
import application.database.interfaces.IUserRepo;
import application.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import application.utils.exceptions.UserException;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class UserRepoImpl extends AbstractRepoImpl<User> implements IUserRepo {

    @Override
    public void createAccount(String username, String password) throws UserException {

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            Transaction transaction = session.beginTransaction();

            try{
                session.save(new User(username, password));
                transaction.commit();
            }catch (Exception e){
                transaction.rollback();
                throw  new UserException(e.getMessage());
            }
        }
    }

    @Override
    public boolean hasAccount(String username, String password) {

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> table = query.from(User.class);

            query.select(table).where(
                    builder.and(
                            builder.equal(
                                    table.get("username"),
                                    username
                            ),
                            builder.equal(
                                    table.get("password"),
                                    password
                            )
                    )
            );


            return !session.createQuery(query).getResultList().isEmpty();
        }
    }

    @Override
    public Optional<User> findUserByUsername(String username) {

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> table = query.from(User.class);

            query.select(table).where(
                    builder.equal(
                            table.get("username"),
                            username
                    )
            );

            return Optional.ofNullable(session.createQuery(query).getSingleResult());
        }catch (NoResultException ignored){
        }

        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            CriteriaQuery<User> query = session.getCriteriaBuilder().createQuery(User.class);

            Root<User> table = query.from(User.class);
            query.select(table);

            return session.createQuery(query).getResultList();
        }
    }
}
