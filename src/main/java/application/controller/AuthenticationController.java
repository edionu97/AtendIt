package application.controller;

import application.messages.ErrorMessage;
import application.messages.request.AuthenticationMessage;
import application.messages.request.ChangePasswordMessage;
import application.messages.request.SetRoleMessage;
import application.messages.response.AuthenticationResponse;
import application.model.User;
import application.utils.exceptions.ErrorMessageException;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import application.service.interfaces.IAuthService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @PostMapping("/info")
    public ResponseEntity<?> getInfo(@RequestBody AuthenticationMessage request){

        if(request == null || request.getUsername() == null){
            return new ResponseEntity<>(
                    new ErrorMessage(
                            HttpStatus.BAD_REQUEST, "Required fields are missing!"
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        Optional<User> userOptional = service.findUserByUsername(request.getUsername());

        return userOptional.<ResponseEntity<?>>map(user -> {
            user.setPassword(null);
            return  new ResponseEntity<>(user, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(
                new ErrorMessage(HttpStatus.NOT_FOUND, "User not found"),
                HttpStatus.NOT_FOUND
        ));
    }

    @GetMapping(value = "/users")
    public ResponseEntity<?> getAllUsers() {
        final Map<String, List<User>> users = new HashMap<>();
        users.putIfAbsent("users", service.getAllUsers());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(value = "/user/set-role")
    public ResponseEntity<?> setRole(@RequestBody SetRoleMessage message) {

        if(message == null || message.getUsername() == null || message.getRole() == null){
            return new ResponseEntity<>(
                    new ErrorMessage(
                            HttpStatus.BAD_REQUEST, "Required fields are missing!"
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        try{
            service.setRole(message.getRole(), message.getUsername());
        }catch (ErrorMessageException ex){
            return new ResponseEntity<>(ex.getErrorMessage(), ex.getErrorMessage().getCode());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private IAuthService service;
}
