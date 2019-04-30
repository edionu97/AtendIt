package application.server;
import application.database.implementation.AttendanceRepoImpl;
import application.database.implementation.UserRepoImpl;
import application.database.interfaces.IAttendanceRepo;
import application.service.CourseService;
import application.utils.model.ClassType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import java.sql.SQLOutput;

@SpringBootApplication
@ComponentScan(basePackages = {"application.controller", "application.notifications"})
public class Main {
    public static void main(String ...args) throws  Exception{
//        new SpringApplicationBuilder(Main.class)
//                .headless(false)
//                .run(args);



//        IAttendanceRepo attendanceRepo = new AttendanceRepoImpl();
//
//        attendanceRepo.getAll().forEach(x ->  System.out.println(x.getAttendanceDate()));


    }
}
