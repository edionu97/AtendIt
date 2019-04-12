package application.controller;

import application.messages.ErrorMessage;
import application.service.interfaces.IStreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.*;
import java.util.Objects;


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

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/update-left-right",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  ResponseEntity<?> putLeftRight(
            @RequestParam(name = "fileLeftRight") MultipartFile leftRightFile, @RequestParam("user") String username, @RequestParam(name="fileUpDown") MultipartFile upDownFile){


        try(OutputStream outputStream = new FileOutputStream(new File(Objects.requireNonNull(leftRightFile.getOriginalFilename())))){

            outputStream.write(leftRightFile.getBytes());
        }catch (Exception ex){
        }

        try(OutputStream outputStream = new FileOutputStream(new File(Objects.requireNonNull(upDownFile.getOriginalFilename())))){

            outputStream.write(upDownFile.getBytes());
        }catch (Exception ex){
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private IStreamingService service;
}
