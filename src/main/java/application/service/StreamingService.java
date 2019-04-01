package application.service;

import application.service.interfaces.IStreamingService;
import artificial_inteligence.detector.YOLOModel;
import artificial_inteligence.video.DetectionCropper;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import utils.image.ImageOps;

import java.util.List;

@Component
@ComponentScan("application/config")
public class StreamingService implements IStreamingService {


    @Autowired
    public StreamingService(
            final DetectionCropper cropper) {
        this.cropper = cropper;
    }

    @Override
    public void identifyObjects(final byte[] bytes) throws Exception {

        final opencv_core.Mat matrix = ImageOps.toCoreMat(
                ImageOps.getMatrixFromBytes(bytes)
        );

        opencv_imgproc.resize(
                matrix, matrix, new opencv_core.Size(416, 416)
        );

        //detect sub-images from image
        final List<Mat> detectedObjects = this.cropper.getDetectedObjects(
                matrix, .4
        );

        //display each bounding boc
        System.out.println(detectedObjects.size());
        for (final Mat mt : detectedObjects) {
            ImageOps.displayImage(mt);
        }

        ImageOps.drawRectangles(
                matrix,
                new YOLOModel().detectObject(matrix, .4),
                true
        );

        ImageOps.displayImage(matrix);
    }

    private final DetectionCropper cropper;
}
