package application.service;

import application.database.interfaces.IAttendanceRepo;
import application.database.interfaces.IHistoryRepo;
import application.database.interfaces.IUserRepo;
import application.model.*;
import application.notifications.nonStomp.WebSocketConfig;
import application.service.interfaces.*;
import application.service.utils.PushNotification;
import application.utils.exceptions.ErrorMessageException;
import application.utils.image_processing.VideoProcessor;
import application.utils.model.ClassType;
import org.bytedeco.javacpp.opencv_core;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import utils.image.ImageOps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@ComponentScan(
        basePackages = {
                "application.config", "application.service"
        }
)
public class AttendanceService implements IAttendanceService {

    @Autowired
    public AttendanceService(
            final IUserRepo userRepo,
            final IHistoryRepo historyRepo,
            final ICourseService courseService,
            final IAttendanceRepo attendanceRepo,
            final IEnrollmentService enrollmentService,
            final IProfileService profileService,
            final IHistoryService historyService,
            final IRecognitionService recognitionService) {
        this.userRepo = userRepo;
        this.historyRepo = historyRepo;
        this.historyService = historyService;
        this.courseService = courseService;
        this.attendanceRepo = attendanceRepo;
        this.enrollmentService = enrollmentService;
        this.profileService = profileService;
        this.recognitionService = recognitionService;
    }

    @Override
    public boolean hasFaceSet(final String username) {
        return userRepo.getUserFaceId(username).isPresent();
    }

    @Override
    public void addAttendance(
            final String studentName,
            final String courseName,
            final ClassType type,
            final String teacherName, final History history) throws ErrorMessageException {

        final Optional<Course> courseOptional = courseService.findCourseBy(
                teacherName, courseName, type
        );

        final Optional<User> studentOptional = userRepo.findUserByUsername(studentName);

        if (!courseOptional.isPresent()) {
            throw new ErrorMessageException(
                    String.format("Course (%s, %s, %s) not found", teacherName, courseName, type),
                    HttpStatus.NOT_FOUND
            );
        }

        attendanceRepo.addAttendance(
                studentOptional.orElse(null),
                courseOptional.get(),
                history
        );
    }

    @Override
    public void modifyAttendance(
            final String courseName,
            final ClassType type,
            final String teacherName,
            final int historyId,
            final List<String> presentsUsername) throws ErrorMessageException {

        //get the history
        final Optional<History> historyOptional = historyService.findById(historyId);
        if (!historyOptional.isPresent()) {
            throw new ErrorMessageException(
                    "In database does not exist such a history",
                    HttpStatus.NOT_FOUND
            );
        }
        final History history = historyOptional.get();

        //delete all the attendances from that specific history
        history.getAttendances().forEach(attendance -> {
            try {
                attendance.setHistory(null);
                attendanceRepo.update(attendance);
                attendanceRepo.delete(attendance);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (attendance.getUser() == null) {
                return;
            }
            //send notification to client
            WebSocketConfig.TopicHandler.pushMessage(
                    attendance.getUser().getUsername(),
                    PushNotification.toJson(new PushNotification("update-attendances"))
            );
        });

        history.setAttendances(new HashSet<>());
        historyRepo.update(history);

        if(presentsUsername.isEmpty()){
            addAttendance(
                    null, courseName, type, teacherName, history
            );
        }

        presentsUsername.forEach(studentUsername -> {
            try {
                addAttendance(
                        studentUsername, courseName, type, teacherName, history
                );
                //send notification to client
                WebSocketConfig.TopicHandler.pushMessage(
                        studentUsername,
                        PushNotification.toJson(new PushNotification("update-attendances"))
                );
            } catch (ErrorMessageException e) {
                e.printStackTrace();
            }
        });
        //send notification to client
        WebSocketConfig.TopicHandler.pushMessage(
                teacherName,
                PushNotification.toJson(new PushNotification("update-attendances"))
        );
    }

    @Override
    public void delete(int attendanceId) throws ErrorMessageException {

        final Optional<Attendance> attendance = attendanceRepo.findById(attendanceId);

        if (!attendance.isPresent()) {
            throw new ErrorMessageException("Attendance not found", HttpStatus.NOT_FOUND);
        }

        attendanceRepo.delete(attendance.get());
    }

    @Override
    public void update(Attendance attendance) throws ErrorMessageException {
        attendanceRepo.update(attendance);
    }

    @Override
    public Optional<Attendance> findById(int id) {
        return attendanceRepo.findById(id);
    }

    @Override
    public List<Attendance> getAll() {
        return attendanceRepo.getAll();
    }

    @Override
    public List<Attendance> getAttendanceFor(String username) {

        final Optional<User> studentOptional = userRepo.findUserByUsername(username);
        return studentOptional.map(
                user -> attendanceRepo
                        .getAllAttendancesFor(user)
                        .stream()
                        .peek(
                                attendance -> attendance.getCourse().getUser().setPassword(null)
                        )
                        .collect(Collectors.toList())
        ).orElseGet(ArrayList::new);
    }

    @Override
    public List<Attendance> getAttendanceForAt(String studentName, String courseName, ClassType type, String teacherName) {
        return attendanceRepo
                .getAttendancesForAt(
                        teacherName, studentName, courseName, type
                ).stream().peek(x -> {
                    try {
                        final Optional<Profile> profileOptional = profileService.getUserProfile(teacherName);

                        if (!profileOptional.isPresent()) {
                            return;
                        }

                        x.getCourse().getUser().setProfile(profileOptional.get());
                    } catch (ErrorMessageException e) {
                        e.printStackTrace();
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public void automaticAttendance(
            final byte[] attendanceVideo, final String teacherName, final String attendanceClass, final String courseName, final ClassType courseType) throws ErrorMessageException {

        //put task on another thread
        executorService.submit(() -> {
            //retrive the frames
            final List<opencv_core.Mat> images = _getFramesFromVideo(attendanceVideo);
            //get labels
            final List<String> labels = recognitionService
                    .getIdentifiedLables(attendanceVideo, .4);

            final History history = new History(attendanceClass, teacherName);
            history.setAttendanceImage(
                    ImageOps.convertMat2Bytes(ImageOps.toMat(images.get(0)))
            );

            System.out.println(recognitionService.getRecognitionConfidence());
            //iterate through all identified labels
            labels.forEach(studentUsername -> {
                try {
                    addAttendance(
                            studentUsername, courseName, courseType, teacherName, history
                    );
                    //send notification to client
                    WebSocketConfig.TopicHandler.pushMessage(
                            studentUsername,
                            PushNotification.toJson(new PushNotification("update-attendances"))
                    );
                } catch (ErrorMessageException e) {
                    e.printStackTrace();
                }
            });

            if (labels.isEmpty()) {
                try {
                    addAttendance(
                            null, courseName, courseType, teacherName, history
                    );
                } catch (ErrorMessageException e) {
                    e.printStackTrace();
                }
            }

            //send notification to teacher
            WebSocketConfig.TopicHandler.pushMessage(
                    teacherName,
                    PushNotification.toJson(new PushNotification("update-attendances"))
            );

            images.forEach(opencv_core.Mat::release);
        });
    }

    private List<opencv_core.Mat> _getFramesFromVideo(final byte[] video) {
        final VideoProcessor videoProcessor = new VideoProcessor();

        //retrive the frames
        List<opencv_core.Mat> images = null;
        try {
            images = videoProcessor.getImagesNotResized(video);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return images;
    }

    private IUserRepo userRepo;
    private IHistoryRepo historyRepo;
    private IHistoryService historyService;
    private ICourseService courseService;
    private IAttendanceRepo attendanceRepo;
    private IProfileService profileService;
    private IEnrollmentService enrollmentService;
    private IRecognitionService recognitionService;
    private ExecutorService executorService = Executors.newFixedThreadPool(20);
}
