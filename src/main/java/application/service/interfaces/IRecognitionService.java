package application.service.interfaces;

import org.opencv.core.Mat;

import java.util.List;
import java.util.Map;

public interface IRecognitionService {

    /**
     *
     * @param video: byte array representing the video from which the faces will be extracted
     * @param treshHold: recognition treshHold, int, representing the minimum percentage of frames in which the face should occur
     * @return: a list with all identified faces from video
     */
    List<String> getIdentifiedLables(final byte[] video, final double treshHold);

    /**
     * Add new face to trained model
     * @param newFaceImages: new face images
     * @param lable: the lable that should be associated whith image
     * @param lableCount: the numeric equivalent of lable
     */
    public void fitToModel(
            final List<Mat> newFaceImages, final String lable, final int lableCount);

    /**
     * @return an association between lable and it's percentage of appeareance
     */
    public Map<String, Double> getRecognitionConfidence();
}
