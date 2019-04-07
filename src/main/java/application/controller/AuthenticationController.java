package application.controller;

import application.messages.ErrorMessage;
import application.messages.request.AuthenticationMessage;
import application.messages.request.ChangePasswordMessage;
import application.messages.response.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import application.service.interfaces.IAuthService;

@RestController
@RequestMapping("/auth")
@ComponentScan(basePackages = "application.service")
public class AuthenticationController {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AuthenticationController(IAuthService service){
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationMessage authMessage){

        if(authMessage == null || authMessage.getPassword() == null || authMessage.getUsername() == null){
            return new ResponseEntity<>(
                    new ErrorMessage(
                            HttpStatus.BAD_REQUEST, "Usern or Passwd fields are missing!"
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        AuthenticationResponse response = service.login(
                authMessage.getUsername(),
                authMessage.getPassword()
        );

        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationMessage message){

        if(message == null || message.getPassword() == null || message.getUsername() == null){
            return new ResponseEntity<>(
                    new ErrorMessage(
                            HttpStatus.BAD_REQUEST, "Usern or Passwd fields are missing!"
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        AuthenticationResponse response = service.createAccount(message);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordMessage request){

        if(request == null || request.getNewPassword() == null || request.getPassword() == null || request.getUsername() == null){
            return new ResponseEntity<>(
                    new ErrorMessage(
                            HttpStatus.BAD_REQUEST, "Usern or Passwd or NewPasswd fields are missing!"
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        try{
            service.changePassword(
                    request.getPassword(),
                    request.getNewPassword(),
                    request.getUsername()
            );
        }catch (Exception ex){
            ErrorMessage message = new ErrorMessage(
                    HttpStatus.NOT_FOUND,
                    ex.getMessage()
            );
            return new ResponseEntity<>(message, message.getCode());
        }

        return  new ResponseEntity<>(HttpStatus.OK);
    }


    private IAuthService service;
}
