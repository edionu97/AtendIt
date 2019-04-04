package artificial_inteligence.video;


import artificial_inteligence.detector.YOLOModel;
import artificial_inteligence.trainer.YOLOTrainer;
import artificial_inteligence.utils.xmls.BndBox;
import nu.pattern.OpenCV;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_videoio;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Mat;
import org.opencv.videoio.Videoio;
import utils.image.ImageOps;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
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

        capture.get().set(CV_CAP_PROP_FRAME_WIDTH, YOLOTrainer.INPUT_WIDTH);
        capture.get().set(CV_CAP_PROP_FRAME_HEIGHT, YOLOTrainer.GRID_HEIGHT);

        if (!capture.get().open(0)) {
            System.out.println("Error");
        }

        opencv_core.Mat image = new opencv_core.Mat();

        CanvasFrame mainframe = new CanvasFrame(
                "Face detector",
                CanvasFrame.getDefaultGamma() / 2.2
        );

        mainframe.setCanvasSize(YOLOTrainer.INPUT_WIDTH, YOLOTrainer.INPUT_HEIGHT);
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

    public void detect(final Mat img) throws Exception {

        final YOLOModel model = new YOLOModel();

        OpenCVFrameConverter.ToMat converter1 = new OpenCVFrameConverter.ToMat();
        OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

        opencv_core.Mat image = converter1.convert(converter2.convert(img));

        opencv_imgproc.resize(
                image, image, new opencv_core.Size(IMAGE_INPUT_W, IMAGE_INPUT_H)
        );


        double st = System.currentTimeMillis();

        List<BndBox> detectedObjs = model.detectObject(image, .4);


        double per = (System.currentTimeMillis() - st) / 1000.0;

        ImageOps.drawRectangles(
                image,
                detectedObjs,
                true
        );

        putText(image, "Detection Time : " + per + " ms", new opencv_core.Point(10, 25), 2, .9, opencv_core.Scalar.YELLOW);

        ImageOps.displayImage(image);
    }

    public void detectOnVideo(final String imagePath) throws Exception {

        OpenCV.loadLocally();
        opencv_core.Mat frame = new opencv_core.Mat();
        opencv_videoio.VideoCapture camera = new opencv_videoio
                .VideoCapture(Paths.get(imagePath)
                .toFile().getPath()
        );

        camera.set(Videoio.CAP_PROP_FRAME_WIDTH, YOLOTrainer.GRID_WIDTH); // width
        camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, YOLOTrainer.INPUT_WIDTH); // height

        JFrame jframe = new JFrame("Check");

        if (!camera.isOpened()) {
            throw new Exception("Camera not opened!");
        }

        jframe.setSize(new Dimension(IMAGE_INPUT_W,  IMAGE_INPUT_H));
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel vidpanel = new JLabel();

        vidpanel.setSize(new Dimension(IMAGE_INPUT_W, IMAGE_INPUT_H));
        jframe.setContentPane(vidpanel);
        jframe.setVisible(true);

        YOLOModel model = new YOLOModel();

        while (camera.read(frame)) {

            opencv_imgproc.resize(
                    frame, frame, new opencv_core.Size(IMAGE_INPUT_W, IMAGE_INPUT_H)
            );

            List<BndBox> detectedObjs = model.detectObject(frame, .4);

            ImageOps.drawRectangles(
                    frame,
                    detectedObjs,
                    true
            );

            ImageIcon image = new ImageIcon(
                    ImageOps.Mat2BufferedImage(ImageOps.toMat(frame))
            );

            vidpanel.setIcon(image);
            vidpanel.repaint();

            try {
                Thread.sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    private static final int IMAGE_INPUT_W = 416;
    private static final int IMAGE_INPUT_H = 416;
    private static final OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
}


