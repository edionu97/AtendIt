package artificial_inteligence;

import artificial_inteligence.detector.YOLOModel;
import artificial_inteligence.utils.TrainFileIterator;
import artificial_inteligence.utils.annotation.Annotation;
import artificial_inteligence.utils.xmls.BndBox;
import artificial_inteligence.utils.xmls.Object;
import artificial_inteligence.utils.xmls.Size;
import artificial_inteligence.utils.xmls.Source;
import artificial_inteligence.video.DetectionCropper;
import artificial_inteligence.video.FaceDetector;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_java;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import utils.ConstantsManager;
import utils.image.ImageOps;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class Main {

    public static void main(String... args) throws Exception {

//        Loader.load(opencv_java.class);
////
//        Mat img = Imgcodecs.imread(
//                "C:\\Users\\Eduard\\Desktop\\str.png"
//        );
//
//        DetectionCropper detectionCropper = new DetectionCropper(new YOLOModel());
//
//        List<Mat> list = detectionCropper.getDetectedObjects(
//                ImageOps.toCoreMat(img), .4
//        );
//
//        for(Mat mat : list){
//            ImageOps.displayImage(mat);
//        }
//
//
//        new FaceDetector().detect(
//                img
//        );

        //ImageOps.displayImage(img);

        //annotateData();

//        final StatsStorage statsStorage = new InMemoryStatsStorage();
//        UIServer
//                .getInstance()
//                .attach(
//                        statsStorage
//                );
//
//        YOLOTrainer.getInstance().doTrain(statsStorage);

        new FaceDetector().play();

    }

    private static void annotateData() throws Exception {

        final ConstantsManager manager = ConstantsManager.getInstance();

        final String folderToProcess = manager.get("imageFolderName");
        final String imageFileDir = manager.get("imageFileParentPath");
        final String annotationDirPath = manager.get("annotationFolderPath");
        final String oldAnnotationDetailsPath = manager.get("oldAnnotationFolderPath");


        TrainFileIterator fileIterator = new TrainFileIterator(
                new File(oldAnnotationDetailsPath)
        );

        File annotationDir = new File(annotationDirPath);

        if (annotationDir.mkdirs()) {
            System.out.println(annotationDir.getName() + " created!");
        } else System.out.println("Folder exists!");

        fileIterator.processFile(
                new File(imageFileDir),
                folderToProcess,
                annotationDir
        );


    }

    private static  void checkAdnotaions() throws  Exception{
        // loop through all the images from that specific folder

        final ConstantsManager manager = ConstantsManager.getInstance();

        final String folderToProcess = manager.get("imageFolderName");
        final String imageFileDir = manager.get("imageFileParentPath");
        final String annotationDirPath = manager.get("annotationFolderPath");
        final String oldAnnotationDetailsPath = manager.get("oldAnnotationFolderPath");


        JAXBContext context = JAXBContext.newInstance(
                Source.class,
                Size.class,
                Object.class,
                BndBox.class,
                Annotation.class
        );

        Unmarshaller unmarshaller = context.createUnmarshaller();


        OpenCVFrameConverter.ToMat converter1 = new OpenCVFrameConverter.ToMat();
        OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();
        File f = new File(imageFileDir + "\\" + folderToProcess);
        for (File image : Objects.requireNonNull(f.listFiles())) {

            if (!image.isFile()) {
                continue;
            }

            final Mat img = Imgcodecs.imread(image.getPath());

            final File annotation = new File(
                    image.getPath().replace("\\images", "\\annotations").replace(".jpg", ".xml")
            );

            Annotation annotation1 = (Annotation) unmarshaller.unmarshal(annotation);

            opencv_core.Mat mat = converter1.convert(converter2.convert(img));

            ImageOps.drawRectangles(
                    mat, annotation1.getObject().stream().map(Object::getBndbox).collect(Collectors.toList()),
                    false
            );

            Thread.sleep(1000);

            ImageOps.displayImage(mat);
        }
    }

}
