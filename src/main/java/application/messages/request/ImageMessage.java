package application.messages.request;

import java.io.Serializable;

public class ImageMessage implements Serializable {

    public ImageMessage(){

    }

    public ImageMessage(String image, String usern) {
        this.image = image;
        this.usern = usern;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsern() {
        return usern;
    }

    public void setUsern(String usern) {
        this.usern = usern;
    }

    private String image;
    private String usern;
}
