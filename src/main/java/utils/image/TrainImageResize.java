package utils.image;

import artificial_inteligence.utils.annotation.Annotation;
import artificial_inteligence.utils.xmls.BndBox;
import artificial_inteligence.utils.xmls.Object;
import artificial_inteligence.utils.xmls.Size;
import artificial_inteligence.utils.xmls.Source;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.bytedeco.javacpp.opencv_imgproc.rectangle;

public class TrainImageResize {

    public TrainImageResize() throws  Exception{

    }

    public void resize(final String filepath, final int width, final int height) throws  Exception{

        final File imagePath = new File(filepath);

        final File annotationPath = new File(
                filepath.replace("\\images", "\\annotations").replace(".jpg", ".xml")
        );

        if(!imagePath.exists()){
            throw new Exception(
                    String.format(
                            "File %s not found", filepath
                    )
            );
        }

        if(!annotationPath.exists()){
            throw new Exception(
                    String.format(
                            "File %s not found", annotationPath
                    )
            );
        }

        final Mat image = Imgcodecs.imread(filepath);

        Annotation annotation = (Annotation)unmarshaller.unmarshal(
                annotationPath
        );

        OpenCVFrameConverter.ToMat converter1 = new OpenCVFrameConverter.ToMat();
        OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

        double h = height / image.size().height;
        double w = width / image.size().width;

        org.bytedeco.javacpp.opencv_core.Mat mt = converter1.convert(converter2.convert(image));

        opencv_imgproc.resize(
                mt, mt, new opencv_core.Size(width, height)
        );

        _processImage(annotation, w, h);

        annotation.getSize().setHeight(height);
        annotation.getSize().setWidth(width);

        _saveImageAndAnnotation(
                mt,
                imagePath,
                annotation,
                annotationPath
        );


//        ImageOps.drawRectangles(
//                mt,
//                annotation.getObject().stream().map(Object::getBndbox).collect(Collectors.toList()),
//                false
//        );
//
//        ImageOps.displayImage(converter2.convert(converter1.convert(mt)));
    }


    private void _processImage(final Annotation annotation, final double w, final  double h){

        List<Object> objects = new ArrayList<>();

        annotation.getObject().forEach(
                object -> {

                    final BndBox bndBox = object.getBndbox();

                    int boxHeight = (bndBox.getXmax() - bndBox.getXmin());
                    int boxWidth = (bndBox.getYmax() - bndBox.getYmin());

                    int x1  = (int)Math.round(w * bndBox.getXmin());
                    int y1  = (int)Math.round(h * bndBox.getYmin());
                    int x2 = (int)Math.round(x1 + boxHeight * w);
                    int y2 = (int)Math.round(y1 + boxWidth * h);

                    objects.add(
                            new Object(
                                    "Face",
                                    new BndBox(
                                            x1, x2, y1, y2
                                    )
                            )
                    );
                }
        );

        annotation.setObject(objects);
    }

    private void _saveImageAndAnnotation(
            final opencv_core.Mat mat, final File imagePath,
            final Annotation annotation, final File annotationPath) throws  Exception{


        OpenCVFrameConverter.ToMat converter1 = new OpenCVFrameConverter.ToMat();
        OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

        Mat image = converter2.convert(converter1.convert(mat));
        Imgcodecs.imwrite(
                imagePath.getPath(),
                image
        );

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        marshaller.marshal(annotation, annotationPath);
    }


    private JAXBContext context = JAXBContext.newInstance(
            Source.class,
            Size.class,
            Object.class,
            BndBox.class,
            Annotation.class
    );

    private Unmarshaller unmarshaller = context.createUnmarshaller();
    private Marshaller marshaller = context.createMarshaller();
}
