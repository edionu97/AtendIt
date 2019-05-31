package application.server;

import application.config.ClassConfigurator;
import application.database.implementation.AttendanceRepoImpl;
import application.database.implementation.CourseRepoImpl;
import application.database.implementation.HistoryRepoImpl;
import application.database.implementation.UserRepoImpl;
import application.model.Attendance;
import application.model.History;
import application.service.interfaces.IAttendanceService;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.ClassType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootApplication
@ComponentScan(basePackages = {"application.controller", "application.notifications"})
public class Main {

    private static ApplicationContext applicationContext;

    public static void main(String... args) throws  Exception {
        applicationContext = new SpringApplicationBuilder(Main.class)
                .headless(false)
                .run(args);
    }
}
