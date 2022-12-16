package ru.develonica.service;

import org.springframework.stereotype.Service;
import ru.develonica.model.entity.TaskProperties;
import ru.develonica.repository.TaskPropertiesRepository;

import java.util.List;

/**
 * Сервис отвечающий за получения данных планирования задач из базы.
 */
@Service
public class TaskPropertiesService {

    private final TaskPropertiesRepository taskPropertiesRepository;

    public TaskPropertiesService(TaskPropertiesRepository taskPropertiesRepository) {
        this.taskPropertiesRepository = taskPropertiesRepository;
    }

    public List<TaskProperties> getTaskProperties() {
        return taskPropertiesRepository.findAll();
    }
}
