package ru.develonica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.develonica.model.entity.NewsSource;

import java.util.List;

/**
 * Запросы в БД об источниках новостей.
 */
public interface NewsSourceRepository extends JpaRepository<NewsSource, Integer> {

    boolean existsByName(String name);
}
