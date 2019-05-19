package application.model.query;

import application.model.Profile;
import application.model.User;
import application.utils.model.UserRoles;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


public class UserPart extends User {

    public UserPart(
            final String username, final UserRoles role, final int userId) {

        super(username, null, role);
        setUserId(userId);
    }
}
