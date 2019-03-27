package artificial_inteligence.utils.xmls;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"name", "pose", "truncated", "difficult", "bndbox"})
public class Object {

    public Object(String name, BndBox bndbox) {
        this.name = name;
        this.difficult = 0;
        this.truncated = 0;
        this.pose = "Unspecified";
        this.bndbox = bndbox;
    }

    public Object(String pose, int truncated, int difficult, String name, BndBox bndbox) {
        this.pose = pose;
        this.truncated = truncated;
        this.difficult = difficult;
        this.name = name;
        this.bndbox = bndbox;
    }

    public  Object(){

    }

    public String getPose() {
        return pose;
    }

    public BndBox getBndbox() {
        return bndbox;
    }

    @XmlElement
    public void setBndbox(BndBox bndbox) {
        this.bndbox = bndbox;
    }

    public void setPose(String pose) {
        this.pose = pose;
    }

    public int getTruncated() {
        return truncated;
    }

    @XmlElement
    public void setTruncated(int truncated) {
        this.truncated = truncated;
    }

    public int getDifficult() {
        return difficult;
    }

    @XmlElement
    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    private String pose;
    private String name;
    private int truncated;
    private int difficult;
    private BndBox bndbox;
}
