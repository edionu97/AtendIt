package application.controller;

import application.messages.ErrorMessage;
import application.messages.request.CreateUpdateMessage;
import application.messages.request.GetProfileInfoMessage;
import application.messages.request.UploadPictureMessage;
import application.model.Profile;
import application.service.interfaces.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@ComponentScan(basePackages = "application.service")
public class ProfileController {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ProfileController(IProfileService service){
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create-update")
    public ResponseEntity<?> profile(@RequestBody CreateUpdateMessage request){

        if(request.getUsername() == null){
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Username field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        final String username = request.getUsername();
        final String firstName = request.getFirstName();
        final String lastName = request.getLastName();
        final String email = request.getEmail();
        final String phoneNumber = request.getPhoneNumber();

        try{
            service.createOrUpdate(
                    username,
                    email,
                    firstName,
                    lastName,
                    phoneNumber
            );
        }catch (Exception ex){
            ErrorMessage message = new ErrorMessage(
                    HttpStatus.NOT_FOUND,
                    ex.getMessage()
            );
            return new ResponseEntity<>(message, message.getCode());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/info")
    public ResponseEntity<?> getProfile(@RequestBody GetProfileInfoMessage request){

        if(request.getUsername() == null){
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Username field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        Profile profile;
        try{
            profile = service.getUserProfile(request.getUsername()).orElse(null);
        }catch (Exception ex){
            ex.printStackTrace();
            ErrorMessage message = new ErrorMessage(
                    HttpStatus.NOT_FOUND,
                    ex.getMessage()
            );
            return new ResponseEntity<>(message, message.getCode());
        }

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }


    @RequestMapping(method =RequestMethod.POST, value = "/upload-picture")
    public  ResponseEntity<?> addPicture(@RequestBody UploadPictureMessage request){

        if(request.getUsern() == null){
            return new ResponseEntity<>(
                    new ErrorMessage(HttpStatus.BAD_REQUEST, "Usern field missing"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try{
            service.uploadProfileImage(
                    request.getUsern(),
                    request.getImage()
            );
        }catch (Exception ex){
            ErrorMessage message = new ErrorMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage()
            );
            return new ResponseEntity<>(message, message.getCode());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private IProfileService service;
}
