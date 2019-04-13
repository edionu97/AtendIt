package application.utils.image_processing;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_java;
import org.hibernate.tool.schema.internal.StandardTableExporter;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import utils.image.ImageOps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class VideoProcessor {

    public List<opencv_core.Mat> getImages(final byte[] image) throws  Exception{

        final File file = Files.createTempFile("Video-", ".mp4").toFile();

        _writeToFile(file, image);
        _addFrames(file);

        if(file.delete()){
            System.out.println(
                    String.format("File %s deleted", file.getPath())
            );
        }

        return images;
    }


    private void _writeToFile(final File file, final byte[] data){
        try(OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(data);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void _addFrames(final File file){

        VideoCapture videoCapture = new VideoCapture(file.getPath());

        final Mat mat = new Mat();
        final opencv_core.Size size = new opencv_core.Size(WIDTH, HEIGHT);

        while (videoCapture.read(mat)){

            final opencv_core.Mat resizedCopy = new opencv_core.Mat();

            // could create memory-leaks
            opencv_imgproc.resize(
                    ImageOps.toCoreMat(mat),
                    resizedCopy,
                    size
            );

            images.add(
                    resizedCopy
            );
        }

        mat.release();
        videoCapture.release();
    }


    public void dispose(){
        images.forEach(opencv_core.Mat::release);
    }

    private List<opencv_core.Mat> images = new ArrayList<>();
    private static final int WIDTH = 416, HEIGHT = 416;
}
