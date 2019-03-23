package service.interfaces;

import messages.response.AuthenticationResponse;

public interface  IAuthService {

    AuthenticationResponse login(String username, String password);

}
