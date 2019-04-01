package application.controller;

import application.messages.ErrorMessage;
import application.messages.request.ImageMessage;
import application.service.interfaces.IStreamingService;
import com.mysql.cj.util.Base64Decoder;
import org.apache.commons.io.FileUtils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utils.image.ImageOps;



@RestController
@RequestMapping("/stream")
@ComponentScan(basePackages = "application.service")
public class StreamingController {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public StreamingController(final IStreamingService service){
        this.service = service;
    }


    @PostMapping("/image")
    public ResponseEntity<?> putImage(@RequestParam MultipartFile file){

        try{

            this.service.identifyObjects(file.getBytes());
        }catch (Exception ex) {
            ErrorMessage message = new ErrorMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage()
            );
            return new ResponseEntity<>(message, message.getCode());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private IStreamingService service;
}
