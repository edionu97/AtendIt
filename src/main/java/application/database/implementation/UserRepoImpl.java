package application.database.implementation;

import application.database.AbstractRepoImpl;
import application.database.interfaces.IUserRepo;
import application.model.Profile;
import application.model.User;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.UserRoles;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class UserRepoImpl extends AbstractRepoImpl<User> implements IUserRepo {

    @Override
    public void createAccount(String username, String password, UserRoles userRoles) throws ErrorMessageException {

        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            Transaction transaction = session.beginTransaction();

            try{
                session.save(new User(username, password, userRoles));
                transaction.commit();
            }catch (Exception e){
                transaction.rollback();
                throw new ErrorMessageException(
                        String.format("User with username: %s already exists.", username), HttpStatus.INTERNAL_SERVER_ERROR
                );
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

    @Override
    public Optional<Profile> getUserProfile(final String username) {
        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            final String HQL =
                    "select " +
                            "new application.model.query.ProfilePart(" +
                                    "p.firstName, " +
                                    "p.lastName, " +
                                    "p.email, " +
                                    "p.phoneNumber, " +
                                    "p.image, " +
                                    "p.imageType, " +
                                    "p.profileId " +
                            ") from User u " +
                        "inner join u.profile p " +
                    "where u.username = :username";

            return session
                    .createQuery(HQL)
                    .setParameter("username", username)
                    .getResultList()
                    .stream()
                    .findFirst();
        }
    }
}
