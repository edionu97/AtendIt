package application.config;

import application.database.implementation.*;
import application.database.interfaces.*;
import artificial_inteligence.detector.IDetector;
import artificial_inteligence.detector.YOLOModel;
import artificial_inteligence.video.DetectionCropper;
import face_recogniser.FaceRecogniser;
import face_recogniser.recognizer.Recognizer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_java;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import sun.misc.BASE64Encoder;

@Configuration
public class ClassConfigurator {

    @Bean
    public IUserRepo userRepo() {
        return new UserRepoImpl();
    }

    @Bean
    public IFaceImagesRepo faceImagesRepo(){
        return  new FaceImagesRepoImpl(
                userRepo()
        );
    }

    @Bean
    public ICourseRepo courseRepo(){
        return  new CourseRepoImpl();
    }

    @Bean
    public IEnrollmentRepo enrollmentRepo(){
        return  new EnrollmentRepoImpl();
    }

    @Bean
    public IAttendanceRepo attendanceRepo(){
        return  new AttendanceRepoImpl();
    }

    @Bean
    public BASE64Encoder encoder() {
        return new BASE64Encoder();
    }

    @Bean
    public IDetector<ComputationGraph> detector() {

//        if(1 == 1){
//            return null;
//        }

        Loader.load(opencv_java.class);
        return new YOLOModel();
    }

    @Bean
    public DetectionCropper cropper() {
        return new DetectionCropper(detector());
    }


    @Bean
    public  Recognizer recognizer(){
        return  new Recognizer();
    }

    @Bean
    public FaceRecogniser faceRecogniser() {
        return new FaceRecogniser(cropper(), recognizer());
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1024 * 1024 * 1024);
        return multipartResolver;
    }
}
