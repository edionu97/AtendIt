package artificial_inteligence.utils.xmls;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {
        "name",
        "pose",
        "truncated",
        "difficult",
        "bndbox"
})
public class Object {

    /**
     * Creating the instance of the class
     * @param name: the name of the bounding box
     * @param bndbox: bounding box
     */
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

    /// Document elements

    @XmlElement
    public void setBndbox(BndBox bndbox) {
        this.bndbox = bndbox;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    @XmlElement
    public void setTruncated(int truncated) {
        this.truncated = truncated;
    }


    public int getDifficult() {
        return difficult;
    }

    public int getTruncated() {
        return truncated;
    }

    public String getPose() {
        return pose;
    }

    public String getName() {
        return name;
    }


    public void setPose(String pose) {
        this.pose = pose;
    }


    public BndBox getBndbox() {
        return bndbox;
    }


    /// StudentClass fields
    private String pose;
    private String name;
    private BndBox bndbox;

    private int truncated;
    private int difficult;
}
