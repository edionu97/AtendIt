package application.database.interfaces;

import application.model.Face;
import application.model.Profile;
import application.model.User;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.UserRoles;

import java.util.List;
import java.util.Optional;

public interface IUserRepo {

    /**
     * Creates an account for a new user
     * @param username: string
     * @param password: string
     * @throws ErrorMessageException : if in application.database exists an user with same username
     */
    void createAccount(String username, String password, UserRoles userRoles) throws ErrorMessageException;

    /**
     * Checks if an user has account
     * @param username: string
     * @param password: string
     * @return true if the user has an account and false otherwise
     */
    boolean hasAccount(String username, String password);

    /**
     * Get the user that has the username equal to de given one
     * @param username: string
     * @return  an optional<user>
     */
    Optional<User> findUserByUsername(String username);


    /**
     * Update the user info
     * @param user: the user that will be updated
     */
    void update(final User user) throws ErrorMessageException;

    /**
     * @return A list with all users from application.database
     */
    List<User> getAllUsers();

    /**
     * @param username: String representing the username
     * @return user profile
     */
    Optional<Profile> getUserProfile(final String username);

    /**
     * Returns user face
     * @param username
     * @return
     */
    Optional<Integer> getUserFaceId(final String username);
}
