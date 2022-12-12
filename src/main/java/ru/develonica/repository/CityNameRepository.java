package ru.develonica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.develonica.model.entity.CityName;

/**
 * Запросы в БД о городах получения погоды.
 */
public interface CityNameRepository extends JpaRepository<CityName, Integer> {

    boolean existsByName(String name);
}
