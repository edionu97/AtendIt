package application.database;

import application.utils.exceptions.ErrorMessageException;
import application.utils.persistence.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;

public abstract class AbstractRepoImpl<T>  {

    public AbstractRepoImpl() {
        persistenceUtils = HibernateUtils.getInstance();
    }

    public void update(final T object) throws ErrorMessageException{
        try (Session session = persistenceUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(object);
                transaction.commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                transaction.rollback();
                throw new  ErrorMessageException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }


    public void delete(final T object) throws ErrorMessageException{
        try (Session session = persistenceUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.delete(object);
                transaction.commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                transaction.rollback();
                throw  new ErrorMessageException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    protected HibernateUtils persistenceUtils;
}
