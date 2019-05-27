package face_recogniser;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_java;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.face.EigenFaceRecognizer;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.FisherFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import scala.Int;
import utils.image.ImageOps;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.opencv.core.CvType.CV_32SC1;
import static org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

public class MainRecogniser {

    public static void main(String... args) {
        Loader.load(opencv_java.class);

        Recognizer recognizer = new Recognizer();

        final File trainingDir = new File("C:\\Users\\Eduard\\Desktop\\faces94");
        final File[] listFile = trainingDir.listFiles();


        int counter = 0;
        Map<Integer, String> map = new HashMap<>();
        for (final File f : listFile) {

            List<Mat> list = new ArrayList<>();
            for (File file : f.listFiles()) {
                Mat img =
                        Imgcodecs.imread(file.getAbsolutePath());
                list.add(img);
            }
            ++counter;
            recognizer.fitToModel(list,f.getName(), counter);
        }

        recognizer.saveModel();

        System.out.println("Train startred");

        Mat testing = Imgcodecs.imread("C:\\Users\\Eduard\\Desktop\\vstros.1.jpg");


        System.out.println(recognizer.predict(testing));

        testing = Imgcodecs.imread("C:\\Users\\Eduard\\Desktop\\9338446.2.jpg");

        System.out.println(recognizer.predict(testing));

//        Mat resize = new Mat();
//        Imgproc.resize(testing, resize, new Size(90, 140));
//
//        int[] arr = new int[1];
//        double[] conf = new double[1];
//        recognizer.predict(resize, arr, conf);
//
//        System.out.println(recognizer.getLabelInfo(arr[0])+ " " + conf[0]);


    }
}
