package application.database;

import application.utils.persistence.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class AbstractRepoImpl<T>  {

    public AbstractRepoImpl() {
        persistenceUtils = HibernateUtils.getInstance();
    }

    public void update(final T object) {
        try (Session session = persistenceUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(object);
                transaction.commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                transaction.rollback();
            }
        }
    }


    public void delete(final T object) throws  Exception{
        try (Session session = persistenceUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.delete(object);
                transaction.commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                transaction.rollback();
                throw  new Exception(ex.getMessage());
            }
        }
    }


    protected HibernateUtils persistenceUtils;
}
