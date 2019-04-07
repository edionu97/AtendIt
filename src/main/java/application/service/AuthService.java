package application.service;


import application.database.interfaces.IUserRepo;
import application.messages.request.AuthenticationMessage;
import application.messages.response.AuthenticationResponse;
import application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import application.service.interfaces.IAuthService;
import sun.misc.BASE64Encoder;
import application.utils.exceptions.UserException;

import java.util.Optional;


@Component
@ComponentScan(basePackages = "application.config")
public class AuthService implements IAuthService {

    @Autowired
    public AuthService(IUserRepo repo, BASE64Encoder encoder){
        this.userRepo = repo;
        this.encoder = encoder;
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

        try{
            this.userRepo.createAccount(username, password);
        }catch (UserException e){
            return new AuthenticationResponse(
                    HttpStatus.IM_USED,
                    e.getMessage()
            );
        }

        return new AuthenticationResponse(
                HttpStatus.OK,
                "Account successfully created!"
        );
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String username) throws UserException {

        Optional<User> user = userRepo.findUserByUsername(username);

        if(!user.isPresent()){
            throw  new UserException(String.format("User %s does not exist", username));
        }

        final User usr = user.get();

        final String encodedPassword = encoder.encode(oldPassword.getBytes());

        if(!encodedPassword.equals(usr.getPassword())){
            throw  new UserException("Password could not be changed: wrong password");
        }

        usr.setPassword(
                encoder.encode(newPassword.getBytes())
        );

        userRepo.update(usr);
    }

    private IUserRepo userRepo;
    private BASE64Encoder encoder;
}
