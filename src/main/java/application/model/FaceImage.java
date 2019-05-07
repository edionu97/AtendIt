package application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class FaceImage implements Serializable {

    public FaceImage() {
    }


    public FaceImage(byte[] image, int height, int width, int type) {
        this.image = image;
        this.height = height;
        this.width = width;
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getType() {
        return type;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFaceImageId() {
        return faceImageId;
    }


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Face getFace() {
        return face;
    }

    public void setFace(Face user) {
        this.face = user;
    }

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int faceImageId;

    @Lob
    @JsonIgnore
    @Column(name="image", columnDefinition="LONGBLOB")
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "faceId")
    @Fetch(FetchMode.JOIN)
    private Face face;

    @Column
    private int height;

    @Column
    private int width;

    @Column
    private int type;
}
