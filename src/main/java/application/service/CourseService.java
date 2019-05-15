package application.service;

import application.database.interfaces.IUserRepo;
import application.model.Course;
import application.model.Enrollment;
import application.model.Profile;
import application.model.User;
import application.service.interfaces.ICourseService;
import application.service.interfaces.IProfileService;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.ClassType;
import application.utils.model.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@ComponentScan(basePackages = "application.config")
public class CourseService implements ICourseService {

    @Autowired
    public CourseService(final IUserRepo userRepo, final IProfileService profileService) {
        this.userRepo = userRepo;
        this.profileService = profileService;
    }

    @Override
    public void addCourse(
            final String username, final String courseName, final String abreviation, final ClassType type) throws ErrorMessageException {

        Optional<User> userOptional = userRepo.findUserByUsername(username);
        if (!userOptional.isPresent()) {
            throw new ErrorMessageException(
                    String.format("User with username: %s not found", username), HttpStatus.NOT_FOUND
            );
        }

        final User user = userOptional.get();
        if (!user.getRole().equals(UserRoles.TEACHER)) {
            throw new ErrorMessageException(
                    String.format("User %s do not have enough permissions", username), HttpStatus.METHOD_NOT_ALLOWED
            );
        }

        // add new course into user's course list
        final Set<Course> courses = user.getCourses();
        courses.add(new Course(
                courseName, type, abreviation
        ));
        user.setCourses(courses);

        userRepo.update(user);
    }

    @Override
    public List<Course> getCoursesFor(String username) {

        Optional<User> userOptional = userRepo.findUserByUsername(username);
        if (!userOptional.isPresent()) {
            return new ArrayList<>();
        }

        final User user = userOptional.get();
        if (!user.getRole().equals(UserRoles.TEACHER)) {
            return new ArrayList<>();
        }

        return user
                .getCourses()
                .stream()
                .map(this::_setCourseProfileImageAndRemoveUserPassword)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Course> findCourseBy(
            final String username, final String name, final ClassType type) {

        Optional<User> userOptional = userRepo.findUserByUsername(username);
        if (!userOptional.isPresent()) {
            return Optional.empty();
        }

        final User user = userOptional.get();
        if (!user.getRole().equals(UserRoles.TEACHER)) {
            return Optional.empty();
        }

        return user
                .getCourses()
                .stream()
                .filter(x -> x.getName().equals(name) && x.getType().equals(type))
                .map(this::_setCourseProfileImageAndRemoveUserPassword)
                .findFirst();
    }

    @Override
    public List<Enrollment> getEnrollmentsAtCourse(final String username, final String name, final ClassType type) {

        final Optional<Course> course = findCourseBy(username, name, type);

        if (!course.isPresent()) {
            return new ArrayList<>();
        }

        final Set<String> set = new HashSet<>();

        return course.get().getEnrollments().stream().peek(enrollment -> {
            //remove the course and the password from result
            enrollment.setCourse(null);
            enrollment.getUser().setPassword(null);
            try {
                final Optional<Profile> profile = profileService.getUserProfile(enrollment.getUser().getUsername());
                if (!profile.isPresent()) {
                    return;
                }
                enrollment.getUser().setProfile(profile.get());
            } catch (ErrorMessageException e) {
                e.printStackTrace();
            }

        }).filter(x -> {
            //keep only distinct values from database
            if (set.contains(x.getUser().getUsername())) {
                return false;
            }
            set.add(x.getUser().getUsername());
            return true;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Course> getAll() {

        return userRepo
                .getAllUsers()
                .stream()
                .filter(
                        x -> x.getRole().equals(UserRoles.TEACHER)
                )
                .map(
                        User::getCourses
                )
                .flatMap(
                        Collection::stream
                )
                .map(this::_setCourseProfileImageAndRemoveUserPassword)
                .collect(
                        Collectors.toList()
                );
    }

    /**
     * Remove the password from user and set's the profile image
     *
     * @param course: the course which will be modified
     * @return same course but with user's password and user's profile image modified
     */
    private Course _setCourseProfileImageAndRemoveUserPassword(final Course course) {

        course.getUser().setPassword(null);

        try {
            final Optional<Profile> profile = profileService.getUserProfile(course.getUser().getUsername());

            if (!profile.isPresent()) {
                return course;
            }

            course.getUser().setProfile(profile.get());
        } catch (ErrorMessageException e) {
            e.printStackTrace();
        }

        return course;
    }

    private IUserRepo userRepo;
    private IProfileService profileService;
}
