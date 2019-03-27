package artificial_inteligence.utils.xmls;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {
        "xmin",
        "ymin",
        "xmax",
        "ymax"
})
public class BndBox {

    /**
     * Constructs a new instance of this class
     * @param xmin: the top corner of the bounding box
     * @param xmax: the bottom corner of the bounding box
     * @param ymin: the top corner high
     * @param ymax: the bottom corner high
     */
    public BndBox(int xmin, int xmax, int ymin, int ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }

    public BndBox(){

    }

    /// Define the elements of the document

    @XmlElement
    public void setXmin(int xmin) {
        this.xmin = xmin;
    }

    @XmlElement
    public void setXmax(int xmax) {
        this.xmax = xmax;
    }

    @XmlElement
    public void setYmin(int ymin) {
        this.ymin = ymin;
    }

    @XmlElement
    public void setYmax(int ymax) {
        this.ymax = ymax;
    }


    public int getXmax() {
        return xmax;
    }

    public int getYmin() {
        return ymin;
    }

    public int getYmax() {
        return ymax;
    }

    public int getXmin() {
        return xmin;
    }

    /// Class fields
    private int xmin;
    private int xmax;
    private int ymin;
    private int ymax;
}
