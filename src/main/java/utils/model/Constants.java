package utils.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Constants {

    public Constants() {
    }

    public String getImageFolderName() {
        return imageFolderName;
    }

    public String getOpencv() {
        return opencv;
    }

    public String getImageFileParentPath() {
        return imageFileParentPath;
    }

    public String getAnnotationFolderPath() {
        return annotationFolderPath;
    }

    public String getOldAnnotationFolderPath() {
        return oldAnnotationFolderPath;
    }

    @XmlElement
    public void setOpencv(String opencv) {
        this.opencv = opencv;
    }

    @XmlElement(name = "image-folder-name")
    public void setImageFolderName(String imageFolderName) {
        this.imageFolderName = imageFolderName;
    }

    @XmlElement(
            name="image-file-parent-path"
    )
    public void setImageFileParentPath(String imageFileParentPath) {
        this.imageFileParentPath = imageFileParentPath;
    }

    @XmlElement(
            name = "annotation-folder-path"
    )
    public void setAnnotationFolderPath(String annotationFolderPath) {
        this.annotationFolderPath = annotationFolderPath;
    }

    @XmlElement(
            name = "old-annotation-folder-path"
    )
    public void setOldAnnotationFolderPath(String oldAnnotationFolderPath) {
        this.oldAnnotationFolderPath = oldAnnotationFolderPath;
    }

    private String opencv;
    private String imageFolderName;
    private String imageFileParentPath;
    private String annotationFolderPath;
    private String oldAnnotationFolderPath;
}
