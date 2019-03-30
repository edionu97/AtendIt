package artificial_inteligence.utils;

import artificial_inteligence.utils.annotation.Annotation;
import artificial_inteligence.utils.annotation.IOldAnnotationParser;
import artificial_inteligence.utils.annotation.OldAnnotationParser;
import artificial_inteligence.utils.xmls.BndBox;
import artificial_inteligence.utils.xmls.Object;
import artificial_inteligence.utils.xmls.Size;
import artificial_inteligence.utils.xmls.Source;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import utils.image.TrainImageResize;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.*;

public class TrainFileIterator {

    public TrainFileIterator(final File filename) throws  Exception{

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            for (String line; ((line = reader.readLine()) != null); ) {
                builder.append(line).append("\n");
            }
        } catch (Exception ignore) {
        }

    }

    /**
     * Loops through over the file and creates for each encountered image a xml file with image details
     * @param fileDirectory: the parent directory of image folder
     * @param imagesDirectory: name of images folders
     * @param annotationDirectory: name of the folder in which the annotations will be saved
     * @throws Exception if file does not exists or something wrong happened
     */
    public void processFile(
            final File fileDirectory,
            final String imagesDirectory, final File annotationDirectory) throws Exception {

        if (!fileDirectory.exists()) {
            throw new Exception("File does not exist");
        }

        for (File file : Objects.requireNonNull(fileDirectory.listFiles())) {

            if (!file.isDirectory()) {
                continue;
            }

            if (!file.getName().equals(imagesDirectory)) {
                continue;
            }

            _parseTrainDirectory(file, "images", annotationDirectory);
            break;
        }

    }

    private void _parseTrainDirectory(
            final File file,
            final String imageDir, final  File annotationDir) throws Exception {

        // loop through all the images from that specific folder
        for (File image : Objects.requireNonNull(file.listFiles())) {

            if (!image.isFile()) {
                continue;
            }

            Mat matrix = Imgcodecs.imread(image.getPath());

            _addAnnotation(
                    imageDir,
                    matrix.size().width,
                    matrix.size().height,
                    builder.toString(),
                    image,
                    annotationDir
            );

        }
    }

    private void _addAnnotation(
            final String outputDirName,
            final double width, final double height,
            final String fileContent, final  File imagePath, final File annotationDir) throws Exception {


        IOldAnnotationParser parser = new OldAnnotationParser(
                fileContent,
                outputDirName,
                (int)width, (int)height
        );


        final String name =  imagePath.getName().replaceAll(".jpg", ".xml");

        _writeAnnotationToFile(
                annotationDir.getPath() + "\\" + name,
                parser.createAnnotation(imagePath)
        );

        resizer.resize(
                imagePath.getPath(),
                RESIZE_WIDTH, RESIZE_HEIGHT
        );
    }


    private void _writeAnnotationToFile(final String dirPath, Annotation annotation) throws Exception {

        final File file = new File(dirPath);

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

        marshaller.marshal(annotation, file);
    }



    private JAXBContext context = JAXBContext.newInstance(
            Source.class,
            Size.class,
            Object.class,
            BndBox.class,
            Annotation.class
    );

    private Marshaller marshaller = context.createMarshaller();

    private StringBuilder builder = new StringBuilder();

    private TrainImageResize resizer = new TrainImageResize();

    private static  final int RESIZE_WIDTH = 416, RESIZE_HEIGHT = 416;
}
