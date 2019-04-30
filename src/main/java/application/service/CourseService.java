package application.service;

import application.database.interfaces.IUserRepo;
import application.model.Course;
import application.model.User;
import application.service.interfaces.ICourseService;
import application.utils.model.ClassType;
import application.utils.model.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ComponentScan(basePackages = "application.config")
public class CourseService implements ICourseService {

    @Autowired
    public CourseService(final IUserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public void addCourse(
            final String username, final String courseName, final ClassType type) throws Exception {

        Optional<User> userOptional = userRepo.findUserByUsername(username);
        if(!userOptional.isPresent()){
            throw  new Exception(
                    String.format("User with username: %s not found", username)
            );
        }

        final User user = userOptional.get();
        if(!user.getRole().equals(UserRoles.TEACHER)){
            throw  new Exception(
                    String.format("User %s do not have enough permissions", username)
            );
        }

        // add new course into user's course list
        final Set<Course> courses = user.getCourses();
        courses.add(new Course(
                courseName, type
        ));
        user.setCourses(courses);

        userRepo.update(user);
    }

    @Override
    public List<Course> getCoursesFor(String username) {

        Optional<User> userOptional = userRepo.findUserByUsername(username);
        if(!userOptional.isPresent()){
            return  new ArrayList<>();
        }

        final User user = userOptional.get();
        if(!user.getRole().equals(UserRoles.TEACHER)){
            return new ArrayList<>();
        }

        return new ArrayList<>(user.getCourses());
    }

    @Override
    public Optional<Course> findCourseBy(
            final String username, final String name, final ClassType type) {

        Optional<User> userOptional = userRepo.findUserByUsername(username);
        if(!userOptional.isPresent()){
            return Optional.empty();
        }

        final User user = userOptional.get();
        if(!user.getRole().equals(UserRoles.TEACHER)){
            return Optional.empty();
        }

        return user
                .getCourses()
                .stream()
                .filter(
                        x -> x.getName().equals(name) && x.getType().equals(type)
                ).findFirst();
    }

    private IUserRepo userRepo;
}
