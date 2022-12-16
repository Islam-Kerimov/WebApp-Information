package ru.develonica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.develonica.model.entity.TaskProperties;

public interface TaskPropertiesRepository extends JpaRepository<TaskProperties, Integer> {
}
