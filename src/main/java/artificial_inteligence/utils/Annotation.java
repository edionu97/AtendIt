package artificial_inteligence.utils;

import artificial_inteligence.utils.xmls.Object;
import artificial_inteligence.utils.xmls.Size;
import artificial_inteligence.utils.xmls.Source;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder={"folder", "filename", "path", "source", "size", "segmented", "object"})
public class Annotation {

    public Annotation(Size size, Object object, String folder, String filename, String path) {
        this.size = size;
        this.object = object;
        this.folder = folder;
        this.filename = filename;
        this.path = path;
        this.source = new Source();
        this.segmented = 0;
    }

    public Annotation(
            Source source, Size size, Object object,
            String folder, String filename, String path,
            int segmented) {
        this.source = source;
        this.size = size;
        this.object = object;
        this.folder = folder;
        this.filename = filename;
        this.path = path;
        this.segmented = segmented;
    }

    public Annotation() {
    }

    public Source getSource() {
        return source;
    }

    @XmlElement
    public void setSource(Source source) {
        this.source = source;
    }

    public Size getSize() {
        return size;
    }

    @XmlElement
    public void setSize(Size size) {
        this.size = size;
    }

    public Object getObject() {
        return object;
    }

    @XmlElement
    public void setObject(Object object) {
        this.object = object;
    }

    public String getFolder() {
        return folder;
    }

    @XmlElement
    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFilename() {
        return filename;
    }

    @XmlElement
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    @XmlElement
    public void setPath(String path) {
        this.path = path;
    }

    public int getSegmented() {
        return segmented;
    }

    @XmlElement
    public void setSegmented(int segmented) {
        this.segmented = segmented;
    }

    private Source source;

    private Size size;

    private Object object;

    private String folder;

    private String filename;

    private String path;

    private int segmented;
}
