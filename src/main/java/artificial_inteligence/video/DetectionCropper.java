package artificial_inteligence.video;

import artificial_inteligence.detector.IDetector;
import artificial_inteligence.utils.xmls.BndBox;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_java;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import utils.image.ImageOps;

import java.util.ArrayList;
import java.util.List;

public class DetectionCropper {

    public DetectionCropper(final IDetector<ComputationGraph> detector) {
        Loader.load(opencv_java.class);
        this.objectDetector = detector;
    }

    public List<Mat> getDetectedObjects(final opencv_core.Mat inputMatrix, final double detection) {

        List<Mat> list = new ArrayList<>();

        try {

            final List<BndBox> boxes = objectDetector.detectObject(
                    inputMatrix,
                    detection
            );

            // add cropped images to image list
            final Mat image = ImageOps.toMat(inputMatrix);
            boxes.forEach(box -> {
                        final int width = box.getXmax() - box.getXmin();
                        final int height = box.getYmax() - box.getYmin();

                        list.add(
                                ImageOps.cropImage(
                                        image,
                                        new Rect(
                                            box.getXmin(), box.getYmin(), width, height
                                        )
                                )
                        );
                    }
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private IDetector<ComputationGraph> objectDetector;
}
