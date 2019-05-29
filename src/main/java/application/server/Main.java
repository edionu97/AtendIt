package application.server;

import application.model.History;
import application.utils.model.ClassType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"application.controller", "application.notifications"})
public class Main {
    public static void main(String... args) {
        new SpringApplicationBuilder(Main.class)
                .headless(false)
                .run(args);
    }
}
