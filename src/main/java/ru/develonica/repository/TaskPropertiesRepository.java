package ru.develonica.repository;

import org.springframework.data.repository.CrudRepository;
import ru.develonica.model.entity.TaskProperties;

public interface TaskPropertiesRepository extends CrudRepository<TaskProperties, Integer> {
}
