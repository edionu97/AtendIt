package artificial_inteligence.utils;

import artificial_inteligence.utils.xmls.BndBox;
import artificial_inteligence.utils.xmls.Object;
import artificial_inteligence.utils.xmls.Size;
import artificial_inteligence.utils.xmls.Source;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

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

        final int indexOfFile = fileContent.indexOf(imagePath.getName());
        int x1, y1, w, h, blur, expression, illumination, invalid, occlusion, pose;

        // Get the next line after the filename
        Scanner reader = new Scanner(
                new BufferedReader(
                        new StringReader(
                                fileContent.substring(indexOfFile + imagePath.getName().length() + 1)
                        )
                ));

        int N = reader.nextInt();

        List<Object> objects = new ArrayList<>();

        for (int i = 0; i < N; ++i) {

            x1 = reader.nextInt();y1 = reader.nextInt();
            w = reader.nextInt();h = reader.nextInt();

            // ignored
            blur = reader.nextInt();expression = reader.nextInt();
            illumination = reader.nextInt();invalid = reader.nextInt();
            occlusion = reader.nextInt();pose = reader.nextInt();

            objects.add(
                    new Object(
                            "Face",
                            new BndBox(
                                    x1, x1 + w, y1, y1 + h
                            )
                    )
            );

        }


        final String name =  imagePath.getName().replaceAll(".jpg", ".xml");

        _writeAnnotationToFile(annotationDir.getPath() + "\\" + name, new Annotation(
                new Size(
                        (int)width, (int)height, 3
                ),
                objects,
                outputDirName,
                imagePath.getName(),
                imagePath.getPath()
        ));
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
}
