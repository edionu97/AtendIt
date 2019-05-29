package application.server;
import application.database.implementation.AttendanceRepoImpl;
import application.database.implementation.HistoryRepoImpl;
import application.model.History;
import application.model.query.HistoryAttendanceCourseStudentPart;
import application.utils.model.ClassType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"application.controller", "application.notifications"})
public class Main {

    public static void main(String ...args){
        new SpringApplicationBuilder(Main.class)
                .headless(false)
                .run(args);
//        HistoryRepoImpl historyRepo = new HistoryRepoImpl();
//
//        for(History h : historyRepo.getAllFor("edi")){
//            final HistoryAttendanceCourseStudentPart part = (HistoryAttendanceCourseStudentPart)h;
//
//            System.out.println(part.getAttendanceImage() + " " + part.getCourseAbr() + " " + part.getCourseName() + " " + part.getRole());
//        }
    }
}
