package application.service;

import application.database.interfaces.IHistoryRepo;
import application.model.History;
import application.model.Profile;
import application.model.User;
import application.model.query.HistoryAttendanceCourseStudentPart;
import application.service.interfaces.IHistoryService;
import application.service.interfaces.IProfileService;
import application.utils.exceptions.ErrorMessageException;
import application.utils.model.ClassType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@ComponentScan(basePackages = {"application.service", "application.config"})
public class HistoryService implements IHistoryService {

    @Autowired
    public HistoryService(final IHistoryRepo historyRepo, final IProfileService profileService) {
        this.historyRepo = historyRepo;
        this.profileService = profileService;
    }

    @Override
    public Optional<History> findById(int historyId) {
        return historyRepo.findHisoryById(historyId);
    }

    @Override
    public List<History> getAllFor(String teacher) {
        return historyRepo.getAllFor(teacher);
    }

    @Override
    public List<History> getAllForAt(String teacher, int id) {
        final List<History> list = historyRepo.getAllForAt(teacher, id);

        list.forEach((hst) -> {
            HistoryAttendanceCourseStudentPart part = (HistoryAttendanceCourseStudentPart) hst;
            try {
                final Optional<Profile> profile = profileService.getUserProfile(part.getStudentName());
                if (profile.isPresent()) {
                    part.setStudentProfile(profile.get());
                }
            } catch (ErrorMessageException e) {
                e.printStackTrace();
            }
        });

        return list;
    }

    @Override
    public Optional<History> findHistoryBy(String teacher, String group) {
        return historyRepo.findHistoryBy(teacher, group);
    }

    @Override
    public List<User> getAbsentUsers(
            final String teacherName,
            final String courseName,
            final ClassType courseType,
            final String grupa,
            final int historyId) {

        return historyRepo.getAbsentUsers(
                teacherName, courseName, courseType, grupa, historyId
        ).stream().peek(x -> {
            try {
                final Optional<Profile> profile = profileService.getUserProfile(x.getUsername());
                if (!profile.isPresent()) {
                    return;
                }
                x.setProfile(profile.get());
            } catch (Exception iggnore) {
            }
        }).collect(Collectors.toList());
    }

    private final IHistoryRepo historyRepo;
    private final IProfileService profileService;
}
