package service;


import database.interfaces.IUserRepo;
import messages.request.AuthenticationMessage;
import messages.response.AuthenticationResponse;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import service.interfaces.IAuthService;
import sun.misc.BASE64Encoder;
import utils.exceptions.UserExeception;


@Component
@ComponentScan(basePackages = "config")
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
        }catch (UserExeception e){
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

    private IUserRepo userRepo;
    private BASE64Encoder encoder;
}
