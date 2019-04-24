package application.service;

import application.database.interfaces.IUserRepo;
import application.model.User;
import application.service.interfaces.IStudentAttendenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ComponentScan("application/config")
public class StudentAttendenceService implements IStudentAttendenceService {

    @Autowired
    public StudentAttendenceService(final IUserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public boolean hasFaceSet(final String username) {
        return userRepo
                .findUserByUsername(username)
                .filter(user -> user.getFace() != null)
                .isPresent();
    }

    private IUserRepo userRepo;
}
