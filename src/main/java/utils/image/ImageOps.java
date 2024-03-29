package utils.image;

import artificial_inteligence.utils.xmls.BndBox;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FrameFilter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import utils.ConstantsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.List;

import static org.bytedeco.javacpp.opencv_imgproc.putText;
import static org.bytedeco.javacpp.opencv_imgproc.rectangle;

public class ImageOps {

    public static void drawRectangles(opencv_core.Mat image, final List<BndBox> objects, final boolean putText) {

        for (BndBox obj : objects) {

            int x1 = obj.getXmin();
            int y1 = obj.getYmin();
            int x2 = obj.getXmax();
            int y2 = obj.getYmax();


            rectangle(
                    image,
                    new opencv_core.Point(x1, y1),
                    new opencv_core.Point(x2, y2),
                    opencv_core.Scalar.GREEN,
                    1, 0, 0
            );

            if (!putText) {
                continue;
            }

            putText(image, "Face", new opencv_core.Point(x1 + 2, y2 - 2), 1, .8, opencv_core.Scalar.GREEN);
        }
    }

    public static Mat cropImage(final Mat mat, final Rect rect) {

        rect.height = (int) Math.min(
                rect.height,
                mat.size().height - rect.y
        );

        rect.width = (int) Math.min(
                rect.width,
                mat.size().width - rect.x
        );


        final double[] size = {rect.width, rect.height};


        Mat img = new Mat(
                mat,
                rect
        );

        img.size().set(size);

        return img;
    }

    public static Mat getMatrixFromBytes(final byte[] bytes) {
        return Imgcodecs.imdecode(
                new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED
        );
    }

    public static void displayImage(final Mat image) {

        Image image1 = Mat2BufferedImage(image);

        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize((int) image.size().width, (int) image.size().height);
        JLabel lbl = new JLabel();

        lbl.setIcon(new ImageIcon(image1));
        frame.add(lbl);
        frame.setVisible(true);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void displayImage(final opencv_core.Mat image) {

        OpenCVFrameConverter.ToMat converter1 = new OpenCVFrameConverter.ToMat();
        OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

        displayImage(converter2.convert(converter1.convert(image)));
    }

    public static opencv_core.Mat toCoreMat(final Mat mat) {

        OpenCVFrameConverter.ToMat converter1 = new OpenCVFrameConverter.ToMat();
        OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

        return converter1.convert(converter2.convert(mat));
    }

    public static Mat toMat(final opencv_core.Mat mat) {

        OpenCVFrameConverter.ToMat converter1 = new OpenCVFrameConverter.ToMat();
        OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

        return converter2.convert(converter1.convert(mat));
    }

    public static BufferedImage Mat2BufferedImage(Mat m) {

        int type = BufferedImage.TYPE_BYTE_GRAY;

        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];

        m.get(0, 0, b);

        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);

        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);

        return image;
    }

    public static byte[] convertMat2Bytes(final Mat mat) {
        try {
            final File file = Files.createTempFile("Image-", ".jpg").toFile();

            Imgcodecs.imwrite(file.getAbsolutePath(), mat);

            final byte[] imageBytes = new byte[(int) file.length()];

            new FileInputStream(file).read(imageBytes);

            if (file.delete()) {
                System.out.println(
                        String.format("File %s deleted", file.getPath())
                );
            }

            return imageBytes;
        } catch (Exception ex) {
            return null;
        }
    }

    public static byte[] convertMat2ByteArray(final Mat mat) {
        final byte[] bytes = new byte[(int) (mat.total() * mat.channels())];
        mat.get(0, 0, bytes);
        return bytes;
    }


    public static Mat bytes2Mat(final byte[] bytes, final int height, final int width, final int type) {
        final Mat mat = new Mat(height, width, type);
        mat.put(0, 0, bytes);
        return mat;
    }
}
