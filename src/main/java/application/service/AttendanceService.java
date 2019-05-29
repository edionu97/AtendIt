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
            final ICourseService courseService,
            final IAttendanceRepo attendanceRepo,
            final IEnrollmentService enrollmentService,
            final IProfileService profileService,
            final IHistoryService historyService,
            final IRecognitionService recognitionService) {
        this.userRepo = userRepo;
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
            final String teacherName, final String group, final opencv_core.Mat frame, final History history) throws ErrorMessageException {

        final Optional<Course> courseOptional = courseService.findCourseBy(
                teacherName, courseName, type
        );

        final Optional<User> studentOptional = userRepo.findUserByUsername(studentName);

        if (!studentOptional.isPresent()) {
            throw new ErrorMessageException(
                    String.format("User with username: %s not found", studentName), HttpStatus.NOT_FOUND
            );
        }

        if (!courseOptional.isPresent()) {
            throw new ErrorMessageException(
                    String.format("Course (%s, %s, %s) not found", teacherName, courseName, type),
                    HttpStatus.NOT_FOUND
            );
        }


        if (!enrollmentService.isEnrolledAtCourse(studentName, courseName, type, teacherName)) {
            throw new ErrorMessageException(
                    String.format("Student %s must enroll first at course (%s, %s, %s)", studentName, teacherName, courseName, type),
                    HttpStatus.FORBIDDEN
            );
        }

        attendanceRepo.addAttendance(
                studentOptional.get(),
                courseOptional.get(),
                history
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
                            studentUsername, courseName, courseType, teacherName, attendanceClass, images.get(0), history
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
            images = videoProcessor.getImages(video);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return images;
    }

    private IUserRepo userRepo;
    private IHistoryService historyService;
    private ICourseService courseService;
    private IAttendanceRepo attendanceRepo;
    private IProfileService profileService;
    private IEnrollmentService enrollmentService;
    private IRecognitionService recognitionService;
    private ExecutorService executorService = Executors.newFixedThreadPool(20);
}
