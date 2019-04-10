package application.config;

import application.database.implementation.UserRepoImpl;
import application.database.interfaces.IUserRepo;
import artificial_inteligence.detector.IDetector;
import artificial_inteligence.detector.YOLOModel;
import artificial_inteligence.video.DetectionCropper;
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
    public BASE64Encoder encoder() {
        return new BASE64Encoder();
    }

    @Bean
    public IDetector<ComputationGraph> detector() {

//        if(1 == 1){
//            return null;
//        }

        return new YOLOModel();
    }

    @Bean
    public DetectionCropper cropper() {
//        if(1 == 1){
//            return null;
//        }
        return new DetectionCropper(detector());
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1024 * 1024 * 1024);
        return multipartResolver;
    }
}
