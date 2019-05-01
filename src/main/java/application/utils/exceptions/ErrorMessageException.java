package application.utils.exceptions;

import application.messages.ErrorMessage;
import org.springframework.http.HttpStatus;

public class ErrorMessageException extends Exception {

    public ErrorMessageException(final String message, final HttpStatus status){
        super(message);
        this.errorMessage = new ErrorMessage(status, message);
    }


    public ErrorMessage getErrorMessage(){
        return  errorMessage;
    }

    private ErrorMessage errorMessage;
}
