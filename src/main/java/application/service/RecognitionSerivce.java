package application.service;

import application.service.interfaces.IRecognitionService;
import face_recogniser.FaceRecogniser;
import face_recogniser.recognizer.Recognizer;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ComponentScan(basePackages = "application.config")
public class RecognitionSerivce implements IRecognitionService {

    @Autowired
    public RecognitionSerivce(final FaceRecogniser faceRecogniser, final Recognizer recognizer){
        this.faceRecogniser = faceRecogniser;
        this.recognizer = recognizer;
    }


    @Override
    public List<String> getIdentifiedLables(final byte[] video, final double treshHold) {
        //process the frames
        faceRecogniser.processFrames(video);
        //get a list with faces that appear in %treshHold percentage of frames
        return faceRecogniser.getIdentifiedFaces(treshHold);
    }

    @Override
    public void fitToModel(final List<Mat> newFaceImages, final String lable, final int lableCount) {
        recognizer.fitToModel(
                newFaceImages, lable, lableCount
        );
        recognizer.saveModel();
    }

    @Override
    public Map<String, Double> getRecognitionConfidence() {
        return faceRecogniser.getFrameFrequcency();
    }

    private FaceRecogniser faceRecogniser;
    private Recognizer recognizer;
}
