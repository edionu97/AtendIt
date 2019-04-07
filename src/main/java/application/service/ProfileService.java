package application.service;

import application.database.interfaces.IUserRepo;
import application.model.Profile;
import application.model.User;
import application.service.interfaces.IProfileService;
import application.utils.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
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
    public Optional<Profile> getUserProfile(String username) throws UserException {

        Optional<User> user = userRepo.findUserByUsername(username);

        if (!user.isPresent()) {
            throw new UserException(
                    String.format("User %s not found", username)
            );
        }

        final Profile profile = user.get().getProfile();

        if (profile == null) {
            return Optional.empty();
        }

        if (profile.getImage() != null) {
            profile.setImageType(
                    profile.getImageType() + "," + Base64.getEncoder().encodeToString(profile.getImage())
            );
        }

        return Optional.of(profile);
    }

    @Override
    public void createOrUpdate(String username, String email, String firstName, String lastName, String phoneNumber) throws UserException {

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

        profile.setImageType(
                profile.getImageType().split(",")[0]
        );

        User user = userRepo.findUserByUsername(username).orElse(null);

        if (user == null) {
            return;
        }

        user.setProfile(profile);

        userRepo.update(user);
    }

    @Override
    public void uploadProfileImage(String username, String base64EncodedImage) throws Exception {

        Optional<Profile> optionalProfile = getUserProfile(username);

        if (!optionalProfile.isPresent()) {
            throw new Exception(String.format("Profile for user %s not found", username));
        }

        Optional<User> userOptional = userRepo.findUserByUsername(username);

        if (!userOptional.isPresent()) {
            throw new Exception(String.format("User %s does not exist", username));
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
