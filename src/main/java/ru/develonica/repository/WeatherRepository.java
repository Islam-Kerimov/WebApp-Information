package ru.develonica.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.develonica.model.entity.CityName;
import ru.develonica.model.entity.Weather;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Запросы в БД о погоде.
 */
public interface WeatherRepository extends PagingAndSortingRepository<Weather, Integer> {

    boolean existsByCityNameAndDateCreate(CityName cityName, OffsetDateTime offsetDateTime);

    @Query(value = "" +
            "SELECT w.* " +
            "FROM weather w JOIN city_name cn " +
                "ON cn.city_id = w.city_id " +
            "WHERE cn.name = :cityName " +
                "AND w.date_create >= :start AND w.date_create <= :end " +
            "ORDER BY w.date_create DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Weather> findBy(String cityName, OffsetDateTime start, OffsetDateTime end);
//    Optional<Weather> findDistinctFirstByCityNameAndDateCreateBetweenOrderByDateCreateDesc(CityName cityName, OffsetDateTime start, OffsetDateTime end);
}
