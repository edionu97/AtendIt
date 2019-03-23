package database.implementation;

import database.interfaces.IUserRepo;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.exceptions.UserExeception;
import utils.persistence.HibernateUtils;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class UserRepoImpl implements IUserRepo {

    public  UserRepoImpl(){
        persistenceUtils = HibernateUtils.getInstance();
    }

    @Override
    public void createAccount(String username, String password) throws UserExeception {

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            Transaction transaction = session.beginTransaction();

            try{
                session.save(new User(username, password));
                transaction.commit();
            }catch (Exception e){
                transaction.rollback();
                throw  new UserExeception(e.getMessage());
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

   private HibernateUtils persistenceUtils;

}
