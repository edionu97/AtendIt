package face_recogniser;

import javafx.util.Pair;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.face.EigenFaceRecognizer;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgproc.Imgproc;
import utils.ConstantsManager;
import utils.image.ImageOps;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_core.solve;


public class Recognizer {

    public Recognizer() {
        _loadModelIfFound();
    }


    private void _loadModelIfFound() {
        final File file = new File(
                ConstantsManager.getInstance().get("faceModel")
        );

        needsTrain = !file.exists();

        if (!file.exists()) {
            System.out.println("Warning: face model not found!");
            return;
        }

        recognizer.read(file.getAbsolutePath());
    }

    /**
     * Predict the face
     *
     * @param image: the face image that needs to be labeled
     * @return: a pair of values representing the face's label and prediction confidence (lower is better)
     */
    public Pair<String, Double> predict(final Mat image) {

        //resize the image
        Imgproc.resize(
                image, image, new Size(FACE_WIDTH, FACE_HEIGHT)
        );
        //convert to grayscale
        Imgproc.cvtColor(
                image, image, Imgproc.COLOR_RGB2GRAY
        );

        //predict face
        final int[] lablePredict = new int[1];
        final double[] confidencePredict = new double[1];
        recognizer.predict(
                image, lablePredict, confidencePredict
        );

        return new Pair<>(
                recognizer.getLabelInfo(lablePredict[0]),
                confidencePredict[0]
        );
    }

    /**
     * Add new data to model, re-trains recogniser with face
     * @param newFaceImages: list of new face images
     * @param lable: the lable that will be assigned to model
     * @param lableCount: the number assoiciated with the lable
     */
    public void fitToModel(final List<Mat> newFaceImages, final String lable, final int lableCount) {

        recognizer.setLabelInfo(lableCount, lable);

        final List<Mat> images = new ArrayList<>();
        final opencv_core.Mat lableMatrix = new opencv_core.Mat(newFaceImages.size(), 1, CV_32SC1);
        final IntBuffer labels = lableMatrix.createBuffer();
        newFaceImages.forEach(image -> {
            //resize the image
            Imgproc.resize(
                    image, image, new Size(FACE_WIDTH, FACE_HEIGHT)
            );
            //convert to grayscale
            Imgproc.cvtColor(
                    image, image, Imgproc.COLOR_RGB2GRAY
            );
            //add image and lables to test
            images.add(image);
            labels.put(
                    images.size() - 1, lableCount
            );
        });

        //if the model does not exist we need to train the recognizer
        if(needsTrain) {
            recognizer.train(images, ImageOps.toMat(lableMatrix));
            needsTrain = false;
            return;
        }

        //otherwise just to update the model
        recognizer.update(images, ImageOps.toMat(lableMatrix));
    }

    public  void saveModel(){
        final File file = new File(ConstantsManager.getInstance().get("faceModel"));
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        recognizer.write(file.getAbsolutePath());
    }

    private  boolean needsTrain = true;
    private FaceRecognizer recognizer = LBPHFaceRecognizer.create();

    private final static int FACE_WIDTH = 90;
    private final static int FACE_HEIGHT = 140;
}
