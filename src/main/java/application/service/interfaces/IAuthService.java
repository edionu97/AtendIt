package application.service.interfaces;

import application.messages.request.AuthenticationMessage;
import application.messages.response.AuthenticationResponse;
import application.utils.exceptions.UserException;

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

    /**
     * Change the password
     * @param oldPassword: the old password
     * @param newPassword: the new password
     * @param username: the username of the user that want to change it's password
     * @throws UserException if oldPassword is not corresponding to saved old password
     */
    void changePassword(String oldPassword, String newPassword, String username) throws UserException;
}
