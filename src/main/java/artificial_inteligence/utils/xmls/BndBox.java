package artificial_inteligence.utils.xmls;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"xmin", "ymin", "xmax", "ymax"})
public class BndBox {

    public BndBox(int xmin, int xmax, int ymin, int ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }

    public BndBox(){

    }

    public int getXmin() {
        return xmin;
    }

    @XmlElement
    public void setXmin(int xmin) {
        this.xmin = xmin;
    }

    public int getXmax() {
        return xmax;
    }

    @XmlElement
    public void setXmax(int xmax) {
        this.xmax = xmax;
    }

    public int getYmin() {
        return ymin;
    }

    @XmlElement
    public void setYmin(int ymin) {
        this.ymin = ymin;
    }

    public int getYmax() {
        return ymax;
    }

    @XmlElement
    public void setYmax(int ymax) {
        this.ymax = ymax;
    }

    private int xmin;
    private int xmax;
    private int ymin;
    private int ymax;
}
