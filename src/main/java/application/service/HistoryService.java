package application.service;

import application.database.interfaces.IHistoryRepo;
import application.model.History;
import application.service.interfaces.IHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HistoryService implements IHistoryService {

    @Autowired
    public HistoryService(final IHistoryRepo historyRepo) {
        this.historyRepo = historyRepo;
    }

    @Override
    public List<History> getAllFor(String teacher) {
        return historyRepo.getAllFor(teacher);
    }

    @Override
    public Optional<History> findHistoryBy(String teacher, String group) {
        return historyRepo.findHistoryBy(teacher, group);
    }

    private final IHistoryRepo historyRepo;
}
