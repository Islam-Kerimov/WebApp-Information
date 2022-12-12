package ru.develonica.service;

import org.springframework.stereotype.Service;
import ru.develonica.model.entity.TaskProperties;
import ru.develonica.repository.TaskPropertiesRepository;

import java.util.ArrayList;
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
        List<TaskProperties> taskProperties = new ArrayList<>();
        taskPropertiesRepository.findAll().forEach(taskProperties::add);

        return taskProperties;
    }
}
