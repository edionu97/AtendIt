package artificial_inteligence.utils.xmls;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {
        "width",
        "height",
        "depth"
})
public class Size {

    /**
     * Create the instance of the class
     * @param width: the height of the picture
     * @param height: the width of the picture
     * @param depth: the picture depth
     */
    public Size(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public Size(){

    }

    ///Document fields

    @XmlElement
    public void setWidth(int width) {
        this.width = width;
    }

    @XmlElement
    public void setHeight(int height) {
        this.height = height;
    }

    @XmlElement
    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public int getWidth() {
        return width;
    }

    //StudentClass fields
    private int width;
    private int height;
    private int depth;
}
