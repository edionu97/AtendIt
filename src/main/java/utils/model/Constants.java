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

    public String getModelPath() {
        return modelPath;
    }

    public String getAcceptedImageWidth() {
        return acceptedImageWidth;
    }

    public String getAcceptedImageHeight() {
        return acceptedImageHeight;
    }

    public String getFaceModel() {
        return faceModel;
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

    @XmlElement(
            name = "model-path"
    )
    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    @XmlElement(
            name = "accepted-image-height"
    )
    public void setAcceptedImageHeight(String acceptedImageHeight) {
        this.acceptedImageHeight = acceptedImageHeight;
    }

    @XmlElement(
            name = "accepted-image-width"
    )
    public void setAcceptedImageWidth(String acceptedImageWidth) {
        this.acceptedImageWidth = acceptedImageWidth;
    }

    @XmlElement(
            name = "face-model"
    )
    public void setFaceModel(String faceModel) {
        this.faceModel = faceModel;
    }

    private String opencv;
    private String modelPath;
    private String faceModel;
    private String imageFolderName;
    private String acceptedImageWidth;
    private String acceptedImageHeight;
    private String imageFileParentPath;
    private String annotationFolderPath;
    private String oldAnnotationFolderPath;
}
