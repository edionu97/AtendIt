package application.server;
import application.database.implementation.AttendanceRepoImpl;
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

//        System.out.println(new AttendanceRepoImpl().getAttendancesForAt(
//                "edi", "onu", "Verificarea si validarea sistemelor soft", ClassType.SEMINAR).get(0).getCourse().getUser().getUsername());

    }
}
