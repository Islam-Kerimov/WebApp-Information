package ru.develonica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.develonica.model.entity.NewsBody;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Запросы в БД о новостях.
 */
public interface NewsBodyRepository extends JpaRepository<NewsBody, Integer> {

    @Query(value = "" +
            "SELECT nb.* " +
            "FROM news_body nb JOIN news_source ns " +
                "ON ns.source_id = nb.source_id " +
            "WHERE ns.name = :source " +
            "ORDER BY nb.public_date DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<NewsBody> findByLast(String source);
    //    Optional<NewsBody> findDistinctFirstByNewsSourceOrderByPublicDateDesc(NewsSource newsSource);

    @Query(value = "" +
            "SELECT nb.* " +
            "FROM news_body nb JOIN news_source ns " +
                "ON ns.source_id = nb.source_id " +
            "WHERE ns.name = :sourceName " +
            "AND nb.public_date >= :start AND nb.public_date <= :end",
            nativeQuery = true)
    List<NewsBody> findAllBy(String sourceName, OffsetDateTime start, OffsetDateTime end);
//    List<NewsBody> findAllByNewsSourceAndPublicDateBetween(NewsSource newsSource, OffsetDateTime start, OffsetDateTime ed);
}
