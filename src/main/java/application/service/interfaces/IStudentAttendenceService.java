package application.service.interfaces;

public interface IStudentAttendenceService {

    /**
     * Returns true if user has a list with face images and false contrary
     * @param username: user's username
     */
    boolean hasFaceSet(final String username);

}
