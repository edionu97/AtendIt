package application.database.implementation;

import application.database.interfaces.IFaceImagesRepo;
import application.database.interfaces.IUserRepo;
import application.model.Face;
import application.model.FaceImage;
import application.model.User;
import application.utils.persistence.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FaceImagesRepoImpl implements IFaceImagesRepo {

    public FaceImagesRepoImpl(final IUserRepo userRepo){
        persistenceUtils = HibernateUtils.getInstance();
        this.userRepo = userRepo;
    }

    @Override
    public void deleteAll(final String username) throws Exception {

        final List<FaceImage> images = getAllFaceImages(username);

        try(final  Session session = persistenceUtils.getSessionFactory().openSession()){

            Transaction transaction = session.beginTransaction();
            try {
                images.forEach(session::delete);
                transaction.commit();
            }catch (Exception ex) {
                transaction.rollback();
                throw new Exception(ex);
            }
        }
    }

    @Override
    public List<FaceImage> getAllFaceImages(final String username) {

        final Optional<User> userOptional = userRepo.findUserByUsername(username);

        if(!userOptional.isPresent()){
            return new ArrayList<>();
        }

        final Face face = userOptional.get().getFace();

        return face != null ? face.getFaces() : new ArrayList<>();
    }

    private IUserRepo userRepo;
    private HibernateUtils persistenceUtils;
}
