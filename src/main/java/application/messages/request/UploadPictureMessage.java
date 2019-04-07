package application.messages.request;

import java.io.Serializable;

public class UploadPictureMessage implements Serializable {

    public UploadPictureMessage() {
    }

    public UploadPictureMessage(String usern, String image) {
        this.usern = usern;
        this.image = image;
    }

    public String getUsern() {
        return usern;
    }

    public void setUsern(String usern) {
        this.usern = usern;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String usern;
    private String image;
}
