package application.config;

import application.database.implementation.AttendanceRepoImpl;
import application.database.implementation.EnrollmentRepoImpl;
import application.database.implementation.FaceImagesRepoImpl;
import application.database.implementation.UserRepoImpl;
import application.database.interfaces.IAttendanceRepo;
import application.database.interfaces.IEnrollmentRepo;
import application.database.interfaces.IFaceImagesRepo;
import application.database.interfaces.IUserRepo;
import artificial_inteligence.detector.IDetector;
import artificial_inteligence.detector.YOLOModel;
import artificial_inteligence.video.DetectionCropper;
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

        if(1 == 1){
            return null;
        }

        Loader.load(opencv_java.class);
        return new YOLOModel();
    }

    @Bean
    public DetectionCropper cropper() {
        return new DetectionCropper(detector());
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1024 * 1024 * 1024);
        return multipartResolver;
    }
}
