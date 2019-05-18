package application.model.query;

import application.model.Profile;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfilePart extends Profile {

    public ProfilePart(
            final String firstName,
            final String lastName,
            final String email,
            final String phoneNumber,
            final byte[] image,
            final String imageType, final int profileId) {

        super(firstName, lastName, email, phoneNumber);
        super.setImageType(imageType);
        super.setImage(image);
        super.setProfileId(profileId);
    }
}
