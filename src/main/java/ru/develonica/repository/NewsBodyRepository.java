package ru.develonica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.develonica.model.entity.NewsBody;
import ru.develonica.model.entity.NewsSource;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Запросы в БД о новостях.
 */
public interface NewsBodyRepository extends JpaRepository<NewsBody, Integer> {

    List<NewsBody> findAllByPublicDateBetweenOrderByPublicDateDesc(OffsetDateTime start, OffsetDateTime end);

    @Query(value = "" +
            "SELECT nb.* " +
            "FROM news_body nb JOIN news_source ns " +
            "ON ns.source_id = nb.source_id " +
            "WHERE ns.url LIKE :name " +
            "AND DATE_TRUNC('day', nb.public_date) BETWEEN :startTime AND :endTime " +
            "ORDER BY nb.public_date DESC ",
            nativeQuery = true)
    List<NewsBody> findAllBySourceBetween(String name, LocalDate startTime, LocalDate endTime);

    @Modifying
    @Query(value = "" +
            "INSERT INTO news_body (source_id, title, category, description, url_news, public_date) " +
            "VALUES ((SELECT DISTINCT nb.source_id " +
            "FROM news_body nb JOIN news_source ns ON ns.source_id = nb.source_id " +
            "WHERE ns.name = :newsName), " +
            ":title, :category, :description, :newsUrl, :publicDate) ",
            nativeQuery = true)
    @Transactional
    void saveBySource(String newsName, String title, String category, String description, String newsUrl, OffsetDateTime publicDate);

    @Query(value = "" +
            "SELECT nb.* " +
            "FROM news_body nb JOIN news_source ns ON ns.source_id = nb.source_id " +
            "WHERE ns.name = :newsName " +
            "ORDER BY nb.public_date DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<NewsBody> findLastBySource(String newsName);
}
