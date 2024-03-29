package application.service;


import application.database.interfaces.IUserRepo;
import application.messages.request.AuthenticationMessage;
import application.messages.response.AuthenticationResponse;
import application.model.Profile;
import application.model.User;
import application.service.interfaces.IProfileService;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import application.service.interfaces.IAuthService;
import sun.misc.BASE64Encoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@ComponentScan(basePackages = "application.config")
public class AuthService implements IAuthService {

    @Autowired
    public AuthService(IUserRepo repo, BASE64Encoder encoder, IProfileService profileService) {
        this.userRepo = repo;
        this.encoder = encoder;
        this.profileService = profileService;
    }


    @Override
    public AuthenticationResponse login(String username, String password) {

        boolean isAuthenticated = this.userRepo.hasAccount(
                username,
                this.encoder.encode(password.getBytes())
        );

        return isAuthenticated ?
                new AuthenticationResponse(HttpStatus.OK, "User is now logged in") :
                new AuthenticationResponse(HttpStatus.NOT_FOUND, "Wrong username or password");
    }

    @Override
    public AuthenticationResponse createAccount(AuthenticationMessage message) {

        String username = message.getUsername();
        String password = encoder.encode(message.getPassword().getBytes());

        try {
            this.userRepo.createAccount(username, password, UserRoles.UNDEFINED);
        } catch (ErrorMessageException e) {
            return new AuthenticationResponse(
                    e.getErrorMessage().getCode(),
                    e.getMessage()
            );
        }

        return new AuthenticationResponse(
                HttpStatus.OK,
                "Account successfully created!"
        );
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String username) throws ErrorMessageException {

        Optional<User> user = userRepo.findUserByUsername(username);

        if (!user.isPresent()) {
            throw new ErrorMessageException(
                    String.format("User %s does not exist", username), HttpStatus.NOT_FOUND
            );
        }

        final User usr = user.get();

        final String encodedPassword = encoder.encode(oldPassword.getBytes());

        if (!encodedPassword.equals(usr.getPassword())) {

            throw new ErrorMessageException(
                    "Password could not be changed: wrong password", HttpStatus.FORBIDDEN
            );
        }

        usr.setPassword(
                encoder.encode(newPassword.getBytes())
        );

        userRepo.update(usr);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepo.findUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {

        final Map<String, Integer> priorities = UserRoles.adminUserPrioriry();

        return userRepo.getAllUsers().stream().peek(user -> {

            Optional<Profile> profile = Optional.empty();

            try {
                profile = profileService.getUserProfile(user.getUsername());
            } catch (ErrorMessageException e) {
                e.printStackTrace();
            }

            if (!profile.isPresent()) {
                return;
            }

            user.setProfile(profile.get());
            user.setPassword(null);

        }).sorted((x, y) -> {
            final Integer firstValue = priorities.get(x.getRole().toString());
            final Integer secondValue = priorities.get(y.getRole().toString());
            return firstValue.compareTo(secondValue);
        }).collect(Collectors.toList());
    }

    @Override
    public void setRole(UserRoles role, String username) throws ErrorMessageException {

        final Optional<User> optionalUser = userRepo.findUserByUsername(username);

        if(!optionalUser.isPresent()){
            throw new ErrorMessageException("User not found", HttpStatus.NOT_FOUND);
        }

        final User user = optionalUser.get();
        user.setRole(role);

        userRepo.update(user);
    }

    private IUserRepo userRepo;
    private IProfileService profileService;
    private BASE64Encoder encoder;
}
