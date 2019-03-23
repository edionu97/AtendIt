package config;

import database.implementation.UserRepoImpl;
import database.interfaces.IUserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.misc.BASE64Encoder;

@Configuration
public class ClassConfigurator {

    @Bean
    public IUserRepo userRepo() {
        return new UserRepoImpl();
    }

    @Bean
    public BASE64Encoder encoder(){
        return  new BASE64Encoder();
    }
}