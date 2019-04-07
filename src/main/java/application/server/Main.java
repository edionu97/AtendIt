package application.server;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "application.controller")
public class Main {
    public static void main(String ...args){
        new SpringApplicationBuilder(Main.class)
                .headless(false)
                .run(args);
        //SpringApplication.run(Main.class, args);
    }
}
