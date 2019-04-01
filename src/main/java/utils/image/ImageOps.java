package utils.image;

import artificial_inteligence.utils.xmls.BndBox;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
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
                    opencv_core.Scalar.RED
            );

            if (!putText) {
                continue;
            }

            putText(image, "Face", new opencv_core.Point(x1 + 2, y2 - 2), 1, .8, opencv_core.Scalar.RED);
        }
    }

    public static Mat cropImage(final Mat mat, final Rect rect){

        final opencv_core.Mat image = toCoreMat(mat);

        return null;
    }


    public static void displayImage(final Mat image) {

        Image image1 = _Mat2BufferedImage(image);

        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize((int) image.size().width, (int) image.size().height);
        JLabel lbl = new JLabel();

        lbl.setIcon(new ImageIcon(image1));
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public  static  void displayImage(final opencv_core.Mat image){

        OpenCVFrameConverter.ToMat converter1 = new OpenCVFrameConverter.ToMat();
        OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

        displayImage(converter2.convert(converter1.convert(image)));
    }

    public static opencv_core.Mat toCoreMat(final Mat mat){

        OpenCVFrameConverter.ToMat converter1 = new OpenCVFrameConverter.ToMat();
        OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

        return converter1.convert(converter2.convert(mat));
    }

    public  static Mat toMat(final opencv_core.Mat mat){

        OpenCVFrameConverter.ToMat converter1 = new OpenCVFrameConverter.ToMat();
        OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

        return converter2.convert(converter1.convert(mat));
    }

    private static BufferedImage _Mat2BufferedImage(Mat m) {

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


}
