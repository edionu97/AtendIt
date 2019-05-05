package application.service.interfaces;

import application.messages.request.AuthenticationMessage;
import application.messages.response.AuthenticationResponse;
import application.model.User;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.UserRoles;

import java.util.List;
import java.util.Optional;

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
     * @throws ErrorMessageException if oldPassword is not corresponding to saved old password
     */
    void changePassword(String oldPassword, String newPassword, String username) throws ErrorMessageException;


    /**
     *
     * @param username: string representing username
     * @return an optional of user
     */
    Optional<User> findUserByUsername(final String username);

    /**
     * @return a list with all users
     */
    List<User> getAllUsers();

    /**
     * Set role to a specific user
     * @param role: the role that will be set
     * @param username: the username
     * @throws ErrorMessageException: if something went wrong
     */
    void setRole(final UserRoles role, final String username) throws  ErrorMessageException;
}
