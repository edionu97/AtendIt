package application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Face implements Serializable {


    public Face(User user) {
        this.user = user;
    }

    public Face() {
    }

    public int getFaceId() {
        return faceId;
    }

    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }

    public List<FaceImage> getFaces() {
        return faces;
    }

    public void setFaces(List<FaceImage> faces) {
        faces.forEach(x -> x.setFace(this));
        this.faces = faces;
    }

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int faceId;


    @JsonIgnore
    @JoinColumn(name = "userId")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "face", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FaceImage> faces = new ArrayList<>();
}
