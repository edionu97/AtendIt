package application.service.interfaces;

import application.model.History;

import java.util.List;
import java.util.Optional;

public interface IHistoryService {
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
}
