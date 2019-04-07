package application.service;

import application.database.interfaces.IUserRepo;
import application.model.Profile;
import application.model.User;
import application.service.interfaces.IProfileService;
import application.utils.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ComponentScan(basePackages = "application.config")
public class ProfileService implements IProfileService {

    @Autowired
    public ProfileService(IUserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public Optional<Profile> getUserProfile(String username) throws UserException {

        Optional<User> user = userRepo.findUserByUsername(username);

        if(!user.isPresent()){
            throw new  UserException(
                    String.format("User %s not found", username)
            );
        }

        return Optional.ofNullable(user.get().getProfile());
    }

    @Override
    public void createOrUpdate(String username, String email, String firstName, String lastName, String phoneNumber) throws UserException {

        Profile profile = getUserProfile(username).orElseGet(
                () -> new Profile(firstName, lastName, email, phoneNumber)
        );

        // Set only those values that are !=  null
        if(firstName != null){
            profile.setFirstName(firstName);
        }
        if(lastName != null){
            profile.setLastName(lastName);
        }
        if(email != null){
            profile.setEmail(email);
        }
        if(phoneNumber != null){
            profile.setPhoneNumber(phoneNumber);
        }

        User user = userRepo.findUserByUsername(username).orElse(null);

        if(user == null){
            return;
        }

        user.setProfile(profile);

        userRepo.update(user);
    }

    private IUserRepo userRepo;
}
