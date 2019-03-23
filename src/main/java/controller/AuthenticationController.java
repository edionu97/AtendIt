package controller;

import messages.request.AuthenticationMessage;
import messages.response.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.AuthService;
import service.interfaces.IAuthService;

@RestController
@RequestMapping("/auth")
@ComponentScan(basePackages = "service")
public class AuthenticationController {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AuthenticationController(IAuthService service){
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationMessage authMessage){

        if(authMessage == null || authMessage.getPassword() == null || authMessage.getUsername() == null){
            return new ResponseEntity<>("Usern or Passwd fields are missing!", HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>("Usern or Passwd fields are missing!", HttpStatus.BAD_REQUEST);
        }

        AuthenticationResponse response = service.createAccount(message);
        return new ResponseEntity<>(response, response.getStatus());
    }


    private IAuthService service;
}
