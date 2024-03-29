package application.service;

import application.database.interfaces.IUserRepo;
import application.model.Profile;
import application.model.User;
import application.service.interfaces.IProfileService;
import application.utils.exceptions.ErrorMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Optional;

@Component
@ComponentScan(basePackages = "application.config")
public class ProfileService implements IProfileService {

    @Autowired
    public ProfileService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Optional<Profile> getUserProfile(String username) throws ErrorMessageException {

        final Optional<Profile> profileOptional = userRepo.getUserProfile(username);

        if (!profileOptional.isPresent()) {
            return Optional.empty();
        }

        final Profile profile = profileOptional.get();
        if (profile.getImage() != null) {
            profile.setImageType(
                    profile.getImageType() + "," + Base64.getEncoder().encodeToString(profile.getImage())
            );
        }

        return Optional.of(profile);
    }

    @Override
    public void createOrUpdate(String username, String email, String firstName, String lastName, String phoneNumber) throws ErrorMessageException {

        Profile profile = getUserProfile(username).orElseGet(
                () -> new Profile(firstName, lastName, email, phoneNumber)
        );

        // Set only those values that are !=  null
        if (firstName != null) {
            profile.setFirstName(firstName);
        }
        if (lastName != null) {
            profile.setLastName(lastName);
        }
        if (email != null) {
            profile.setEmail(email);
        }
        if (phoneNumber != null) {
            profile.setPhoneNumber(phoneNumber);
        }

        if(profile.getImageType() != null){
            profile.setImageType(
                    profile.getImageType().split(",")[0]
            );
        }

        User user = userRepo.findUserByUsername(username).orElse(null);

        if (user == null) {
            return;
        }

        user.setProfile(profile);

        userRepo.update(user);
    }

    @Override
    public void uploadProfileImage(String username, String base64EncodedImage) throws ErrorMessageException {

        Optional<Profile> optionalProfile = getUserProfile(username);

        if (!optionalProfile.isPresent()) {
            throw  new ErrorMessageException(
                    String.format("Profile for user %s not found", username), HttpStatus.NOT_FOUND
            );
        }

        Optional<User> userOptional = userRepo.findUserByUsername(username);

        if (!userOptional.isPresent()) {
            throw  new ErrorMessageException(
                    String.format("User %s does not exist", username), HttpStatus.NOT_FOUND
            );
        }

        String[] headerAndImage = base64EncodedImage.split(",");
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(headerAndImage[1]);

        final Profile profile = optionalProfile.get();

        final User user = userOptional.get();

        profile.setImage(imageBytes);
        profile.setImageType(headerAndImage[0]);
        user.setProfile(profile);
        userRepo.update(user);
    }

    private IUserRepo userRepo;
}
