package application.service;

import application.database.interfaces.IFaceImagesRepo;
import application.database.interfaces.IUserRepo;
import application.model.Face;
import application.model.FaceImage;
import application.model.User;
import application.service.interfaces.IRecognitionService;
import application.service.interfaces.IStreamingService;
import application.utils.image_processing.VideoProcessor;
import artificial_inteligence.detector.YOLOModel;
import artificial_inteligence.video.DetectionCropper;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import utils.image.ImageOps;

import java.awt.*;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@ComponentScan(basePackages = {"application/config", "application/service"})
public class StreamingService implements IStreamingService {

    @Autowired
    public StreamingService(
            final DetectionCropper cropper,
            final IUserRepo userRepo,
            final IFaceImagesRepo faceImagesRepo,
            final IRecognitionService recognitionService) {

        this.userRepo = userRepo;
        this.cropper = cropper;
        this.faceImagesRepo = faceImagesRepo;
        this.recognitionService = recognitionService;
    }

    @Override
    public void identifyObjects(final byte[] bytes) throws Exception {

        final opencv_core.Mat matrix = ImageOps.toCoreMat(
                ImageOps.getMatrixFromBytes(bytes)
        );

        opencv_imgproc.resize(
                matrix, matrix, new opencv_core.Size(416, 416)
        );

        //detect sub-images from image
        final List<Mat> detectedObjects = this.cropper.getDetectedObjects(
                matrix, .4
        );

        //display each bounding boc
        System.out.println(detectedObjects.size());
        for (final Mat mt : detectedObjects) {
            ImageOps.displayImage(mt);
        }

        ImageOps.drawRectangles(
                matrix,
                new YOLOModel().detectObject(matrix, .4),
                true
        );

        ImageOps.displayImage(matrix);
    }

    @Override
    public void uploadUserVideo(final byte[] leftRight, final String username, final byte[] upDown) throws Exception {

        userFaces = new ArrayList<>();

        final VideoProcessor leftRightProcessor = new VideoProcessor();
        final VideoProcessor upDownProcessor = new VideoProcessor();

        final List<opencv_core.Mat> leftFrames = leftRightProcessor.getImages(leftRight);
        final List<opencv_core.Mat> upFrames = upDownProcessor.getImages(upDown);

        _findAllFacesFromVideo(leftFrames, true);
        _findAllFacesFromVideo(upFrames, false);
        _setFaces(username);

        userFaces.forEach(Mat::release);
        leftRightProcessor.dispose();
        upDownProcessor.dispose();
    }

    private void _findAllFacesFromVideo(final List<opencv_core.Mat> frames, boolean isLeftRight) {

        final int PHONE_REGISTER_TIME = 10;
        final int REDUCTION_RATE = 4;
        final int FRAMES_PER_SEC = (frames.size() / PHONE_REGISTER_TIME);

        // jump over FRAMES_PER_SEC / REDUCTION_RATE frames from each step
        for (int i = 0; i < frames.size(); i += (FRAMES_PER_SEC / REDUCTION_RATE)) {

            final opencv_core.Mat frame = frames.get(i);
            final List<Mat> faces = cropper.getDetectedObjects(frame, .4);

            //debug
            __saveDetectionIntoFileDEBUG(i, faces, isLeftRight);

            userFaces.addAll(faces);
        }
    }

    private void _setFaces(final String username) throws Exception {

        // delete previous face images if now there are some new images
        if (!userFaces.isEmpty()) {
            faceImagesRepo.deleteAll(username);
        }

        final Optional<User> userOpt = userRepo.findUserByUsername(username);

        if (!userOpt.isPresent()) {
            throw new Exception(
                    String.format("User %s not found", username)
            );
        }

        final User user = userOpt.get();

        //retrain the model to accept the new user face
        recognitionService.fitToModel(
                userFaces, user.getUsername(), user.getUserId()
        );

        Face face = user.getFace() == null ? new Face() : user.getFace();

        // add user faces into list and set all face images
        final List<FaceImage> faceImages = new ArrayList<>();
        userFaces.forEach(userFace -> {
            // create the face
            final FaceImage image = new FaceImage(
                    ImageOps.convertMat2ByteArray(
                            userFace
                    ),
                    userFace.rows(), userFace.cols(), userFace.type()
            );
            faceImages.add(image);
        });
        face.setFaces(faceImages);

        //set the user face
        user.setFace(face);
        userRepo.update(user);
    }

    private void __saveDetectionIntoFileDEBUG(int i, final List<Mat> faces, final boolean isFrontal) {

        for (int j = 0; j < faces.size(); ++j) {
            final File f = new File(
                    "C:\\Users\\Eduard\\Desktop\\Detected\\" + (isFrontal ? "left_right_" : "up_down_") + i + "_" + j + ".jpg");
            final Mat face = faces.get(j);
            Imgcodecs.imwrite(f.getPath(), face);
        }
    }


    private List<Mat> userFaces;

    private final DetectionCropper cropper;

    private final IUserRepo userRepo;

    private final IFaceImagesRepo faceImagesRepo;
    private final IRecognitionService recognitionService;
}
