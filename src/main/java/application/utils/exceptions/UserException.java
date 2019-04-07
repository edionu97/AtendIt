package application.utils.exceptions;

public class UserException extends  Exception {

    /**
     * Custom exception thrown when a user exception appears
     * @param message: string representing the displayed message
     */
    public UserException(String message){
        super(message);
    }
}
