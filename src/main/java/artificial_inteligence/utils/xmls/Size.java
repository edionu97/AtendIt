package artificial_inteligence.utils.xmls;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"width", "height", "depth"})
public class Size {

    public Size(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public Size(){

    }

    public int getWidth() {
        return width;
    }

    @XmlElement
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    @XmlElement
    public void setHeight(int height) {
        this.height = height;
    }

    public int getDepth() {
        return depth;
    }

    @XmlElement
    public void setDepth(int depth) {
        this.depth = depth;
    }

    private int width;
    private int height;
    private int depth;
}
