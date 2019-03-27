package artificial_inteligence.utils;

import artificial_inteligence.utils.xmls.Object;
import artificial_inteligence.utils.xmls.Size;
import artificial_inteligence.utils.xmls.Source;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement
@XmlType(propOrder = {
        "folder",
        "filename",
        "path",
        "source",
        "size",
        "segmented",
        "object"
})
public class Annotation {

    public Annotation(
            Size size,
            List<Object> object,
            String folder,
            String filename, String path) {
        this.size = size;
        this.object = object;
        this.folder = folder;
        this.filename = filename;
        this.path = path;
        this.source = new Source();
        this.segmented = 0;
    }

    public Annotation(
            Source source, Size size, List<Object> object,
            String folder, String filename, String path, int segmented) {
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


    //Create the document elements

    @XmlElement
    public void setSource(Source source) {
        this.source = source;
    }

    @XmlElement
    public void setSize(Size size) {
        this.size = size;
    }

    @XmlElement
    public void setObject(List<Object> object) {
        this.object = object;
    }


    @XmlElement
    public void setFolder(String folder) {
        this.folder = folder;
    }


    @XmlElement
    public void setFilename(String filename) {
        this.filename = filename;
    }

    @XmlElement
    public void setPath(String path) {
        this.path = path;
    }

    @XmlElement
    public void setSegmented(int segmented) {
        this.segmented = segmented;
    }


    public List<Object> getObject() {
        return object;
    }

    public String getPath() {
        return path;
    }

    public int getSegmented() {
        return segmented;
    }

    public String getFilename() {
        return filename;
    }

    public String getFolder() {
        return folder;
    }

    public Source getSource() {
        return source;
    }

    public Size getSize() {
        return size;
    }

    
    private Source source;
    private Size size;
    private List<Object> object;
    private String folder;
    private String filename;
    private String path;
    private int segmented;
}
