package application.service.interfaces;


public interface IStreamingService {

    void identifyObjects(final byte[] bytes) throws Exception;

    void uploadUserVideo(final byte[] leftRight, final String username, final byte[] upDown) throws Exception;
}
