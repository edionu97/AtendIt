package artificial_inteligence.video;


import artificial_inteligence.detector.YOLOModel;
import artificial_inteligence.utils.xmls.BndBox;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_videoio;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import utils.image.ImageOps;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_videoio.CV_CAP_PROP_FRAME_WIDTH;
import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_HEIGHT;


public class FaceDetector {

    public void play() throws Exception {

        YOLOModel detector = new YOLOModel();

        final AtomicReference<opencv_videoio.VideoCapture> capture = new AtomicReference<>(
                new opencv_videoio.VideoCapture()
        );

        capture.get().set(CV_CAP_PROP_FRAME_WIDTH, 416);
        capture.get().set(CV_CAP_PROP_FRAME_HEIGHT, 416);

        if (!capture.get().open(0)) {
            System.out.println("Error");
        }

        opencv_core.Mat image = new opencv_core.Mat();

        CanvasFrame mainframe = new CanvasFrame(
                "Face detector",
                CanvasFrame.getDefaultGamma() / 2.2
        );

        mainframe.setCanvasSize(416, 416);
        mainframe.setLocationRelativeTo(null);
        mainframe.setVisible(true);
        mainframe.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        while (true) {

            while (capture.get().read(image) && mainframe.isVisible()) {

                long st = System.currentTimeMillis();
                opencv_imgproc.resize(
                        image, image, new opencv_core.Size(IMAGE_INPUT_W, IMAGE_INPUT_H)
                );

                List<BndBox> detectedObjs = detector.detectObject(image, .4);

                ImageOps.drawRectangles(
                        image,
                        detectedObjs,
                        true
                );

                double per = (System.currentTimeMillis() - st) / 1000.0;
                putText(image, "Detection Time : " + per + " ms", new opencv_core.Point(10, 25), 2, .9, opencv_core.Scalar.YELLOW);

                mainframe.showImage(converter.convert(image));

                Thread.sleep(20);

            }
        }
    }


    private static final int IMAGE_INPUT_W = 416;
    private static final int IMAGE_INPUT_H = 416;
    private static final OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
}


