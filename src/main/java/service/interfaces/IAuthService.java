package service.interfaces;

import messages.request.AuthenticationMessage;
import messages.response.AuthenticationResponse;

public interface  IAuthService {

    /**
     * Performs user login
     * @param username: string
     * @param password: string
     * @return an instance of AuthenticationResponse class which contains the operation's result
     */
    AuthenticationResponse login(String username, String password);

    /**
     * Create an user account
     * @param message: an entity that contains all needed user data
     * @return an instance of AuthenticationResponse class which contains the operation's result
     */
    AuthenticationResponse createAccount(AuthenticationMessage message);
}
