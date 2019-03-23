package utils.exceptions;

public class UserExeception extends  Exception {

    /**
     * Custom exception thrown when a user exception appears
     * @param message: string representing the displayed message
     */
    public UserExeception(String message){
        super(message);
    }
}
