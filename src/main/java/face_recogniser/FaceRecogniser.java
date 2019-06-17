package face_recogniser;

import application.utils.image_processing.VideoProcessor;
import artificial_inteligence.video.DetectionCropper;
import face_recogniser.recognizer.Recognizer;
import javafx.util.Pair;
import org.bytedeco.javacpp.opencv_core;
import org.opencv.core.Mat;
import org.opencv.face.FaceRecognizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import utils.image.ImageOps;

import java.util.*;

public class FaceRecogniser {

    public FaceRecogniser(
            final DetectionCropper cropper,
            final  Recognizer recognizer) {
        this.cropper = cropper;
        this.recognizer = recognizer;
    }

    public void processFrames(final byte[] videoFrames) {

        //reinitialize the map
        frameFrequcency = new TreeMap<>();
        frameNo = 0;

        //get frames from video
        final List<opencv_core.Mat> images = _getFramesFromVideo(videoFrames);

        //loop through all frames
        for(final opencv_core.Mat image : images) {
            final List<Mat> faces = cropper.getDetectedObjects(image, .4);

            if(!faces.isEmpty()){
                ++frameNo;
            }

            _processFrame(faces);
            faces.forEach(Mat::release);
        }

        images.forEach(opencv_core.Mat::release);
    }

    public List<String> getIdentifiedFaces(final double pictureTreshold) {

        final List<String> labels = new ArrayList<>();

        //remove false positives from predictions
        frameFrequcency.forEach((label, frameCount) -> {
            frameFrequcency.put(label, (double)frameCount / frameNo);
            if(frameFrequcency.get(label) < pictureTreshold){
                return;
            }
            labels.add(label);
        });

        return labels;
    }

    public Map<String, Double> getFrameFrequcency() {
        return frameFrequcency;
    }


    private void _processFrame(final List<Mat> faces) {

        int frameCount = 1;
        for (final Mat face : faces) {
            frameCount = Math.max(frameCount, 0);
            //classify the face and obtain a prediction
            final Pair<String, Double> prediction = recognizer.predict(face);
            //process face if the first time
            frameFrequcency.computeIfAbsent(prediction.getKey(), k -> .0);
            //increment the number of frames in which face appear (one per method call)
            frameFrequcency.put(
                    prediction.getKey(),
                    frameFrequcency.get(prediction.getKey()) + frameCount--
            );
        }
    }

    private List<opencv_core.Mat> _getFramesFromVideo(final byte[] video) {
        final  VideoProcessor videoProcessor = new VideoProcessor();

        //retrive the frames
        List<opencv_core.Mat> images = null;
        try {
            images = videoProcessor.getImages(video);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return images;
    }

    private  int frameNo;
    private Recognizer recognizer;
    private DetectionCropper cropper;
    private Map<String, Double> frameFrequcency;
}
