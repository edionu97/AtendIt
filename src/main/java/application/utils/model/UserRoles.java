package application.utils.model;

import java.util.HashMap;
import java.util.Map;

public enum UserRoles {
    TEACHER, STUDENT, ADMIN, UNDEFINED;

    public static  Map<String, Integer> adminUserPrioriry(){

        final Map<String, Integer> priorities = new HashMap<>();

        priorities.put(UNDEFINED.toString(), 0);
        priorities.put(ADMIN.toString(), 1);
        priorities.put(TEACHER.toString(), 2);
        priorities.put(STUDENT.toString(), 2);

        return priorities;
    }
}
