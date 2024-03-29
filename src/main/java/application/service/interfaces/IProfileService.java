package application.service.interfaces;

import application.model.Profile;
import application.utils.exceptions.ErrorMessageException;

import java.util.Optional;

public interface IProfileService {

    Optional<Profile> getUserProfile(final String username) throws ErrorMessageException;

    void createOrUpdate(
            final String username,
            final String email, final String firstName, final String lastName, final String phoneNumber
    ) throws  ErrorMessageException;

    void uploadProfileImage(String username, String base64EncodedImage) throws ErrorMessageException;

}
