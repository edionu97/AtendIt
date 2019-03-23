package config;

import database.implementation.UserRepoImpl;
import database.interfaces.IUserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClassConfigurator {

    @Bean
    public IUserRepo userRepo(){
        return new UserRepoImpl();
    }
}
