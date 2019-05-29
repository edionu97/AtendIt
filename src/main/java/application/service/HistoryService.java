package application.service;

import application.database.interfaces.IHistoryRepo;
import application.model.History;
import application.model.Profile;
import application.model.query.HistoryAttendanceCourseStudentPart;
import application.service.interfaces.IHistoryService;
import application.service.interfaces.IProfileService;
import application.utils.exceptions.ErrorMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@ComponentScan(basePackages = {"application.service", "application.config"})
public class HistoryService implements IHistoryService {

    @Autowired
    public HistoryService(final IHistoryRepo historyRepo, final IProfileService profileService) {
        this.historyRepo = historyRepo;
        this.profileService = profileService;
    }

    @Override
    public List<History> getAllFor(String teacher) {
        return historyRepo.getAllFor(teacher);
    }

    @Override
    public List<History> getAllForAt(String teacher, int id) {
        final List<History> list = historyRepo.getAllForAt(teacher, id);

        list.forEach((hst)->{
            HistoryAttendanceCourseStudentPart part = (HistoryAttendanceCourseStudentPart)hst;
            try {
                final Optional<Profile> profile = profileService.getUserProfile(part.getStudentName());
                if(profile.isPresent()){
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

    private final IHistoryRepo historyRepo;
    private final IProfileService profileService;
}
