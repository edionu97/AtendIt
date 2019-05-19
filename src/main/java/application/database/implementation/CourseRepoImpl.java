package application.database.implementation;

import application.database.AbstractRepoImpl;
import application.database.interfaces.ICourseRepo;
import application.model.Course;
import application.model.User;
import application.model.query.CoursePart;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseRepoImpl extends AbstractRepoImpl<Course> implements ICourseRepo {

    @Override
    public List<Course> getAll() {
        try(Session session = persistenceUtils.getSessionFactory().openSession()){

            final  String HQL =
                    "select new application.model.query.CoursePart(" +
                            "c.name, " +
                            "c.type, " +
                            "c.abbreviation, " +
                            "c.courseId" +
                    ") " +
                    "from Course c";

            String USER_HQL =
                    "select new application.model.query.UserPart(" +
                            "u.username, " +
                            "u.role,"+
                            "u.userId" +
                            ")" +
                    "from Course c inner join c.user u where c.courseId = :courseId";

            //set coursePart users
            final List<Course> courses = session.createQuery(HQL).getResultList();
            for(final Course course : courses) {

                final User teacher = session
                        .createQuery(USER_HQL, User.class)
                        .setParameter("courseId", course.getCourseId()).getSingleResult();

                course.setUser(teacher);
            }

            return courses;
        }
    }
}
