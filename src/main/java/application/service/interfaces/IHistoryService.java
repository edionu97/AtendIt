package application.service.interfaces;

import application.model.History;
import application.model.User;
import application.utils.model.ClassType;

import java.util.List;
import java.util.Optional;

public interface IHistoryService {

    /**
     * Returns an optional of history if in database exists an record with
     * historyId = @param historyIf
     * @param historyId: the history's id
     * @return: optional.empty() or optional.of(history)
     */
    Optional<History> findById(final int historyId);

    /**
     * Returns a list with all attendace history for teacher with teachername = @param teacher
     * @param teacher:  theacher's username
     */
    List<History> getAllFor(final String teacher);

    /**
     * Returns a list with all attendace history for teacher with teachername = @param teacher
     * @param teacher:  theacher's username
     */
    List<History> getAllForAt(final String teacher, final int id);

    /**
     * @param teacher: the teacher name
     * @param group: the group number
     * @return: optiona.empty if in database does not exist such an user or optional.of(history) otherwise
     */
    Optional<History> findHistoryBy(final String teacher, final String group);


    /**
     * Get those users that are absent at a specified couse
     * @param teacherName: teacher's name
     * @param courseName: course's name
     * @param courseType: course's type
     * @param grupa: course's group
     * @param historyId: history id
     * @return a list with those users (partial user)
     */
    List<User> getAbsentUsers(
            final String teacherName, final String courseName, final ClassType courseType, final String grupa, final int historyId);
}
