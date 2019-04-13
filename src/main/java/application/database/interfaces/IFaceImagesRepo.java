package application.database.interfaces;

import application.model.FaceImage;

import java.util.List;

public interface IFaceImagesRepo {

    /**
     * Deletes all the information from the face images repository
     * @throws Exception: if something went wrong
     */
    void deleteAll(final String username) throws Exception;

    /**
     * @return a list with all face images from database
     */
    List<FaceImage> getAllFaceImages(final String username);
}
