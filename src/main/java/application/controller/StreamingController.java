package application.controller;

import application.messages.ErrorMessage;
import application.service.interfaces.IAttendanceService;
import application.service.interfaces.IStreamingService;
import application.utils.exceptions.ErrorMessageException;
import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@RestController
@RequestMapping("/stream")
@ComponentScan(basePackages = "application.service")
public class StreamingController {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public StreamingController(final IStreamingService service, final IAttendanceService attendanceService) {
        this.service = service;
        this.attendanceService = attendanceService;
    }


    @PostMapping("/image")
    public ResponseEntity<?> putImage(@RequestParam MultipartFile file) {

        try {

            this.service.identifyObjects(file.getBytes());
        } catch (Exception ex) {
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
    public ResponseEntity<?> putLeftRight(
            @RequestParam(name = "fileLeftRight") MultipartFile leftRightFile,
            @RequestParam("user") String username,
            @RequestParam(name = "fileUpDown") MultipartFile upDownFile) {


        try {
            this.service.uploadUserVideo(
                    leftRightFile.getBytes(),
                    username,
                    upDownFile.getBytes()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
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
            value = "/attendance-video",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAttendanceVideo(
            @RequestParam(value = "video") MultipartFile attendanceVideo,
            @RequestParam(value = "teacher") String teacher,
            @RequestParam(value = "cls") String attendanceClass) {

        try {
            attendanceVideo.transferTo(new File("video.mp4"));
            attendanceService.automaticAttendance(
                    attendanceVideo.getBytes(), teacher, attendanceClass
            );
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private IAttendanceService attendanceService;
    private IStreamingService service;
}
