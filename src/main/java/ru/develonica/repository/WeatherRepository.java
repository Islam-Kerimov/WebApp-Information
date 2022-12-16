package ru.develonica.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.develonica.model.entity.CityName;
import ru.develonica.model.entity.Weather;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Запросы в БД о погоде.
 */
public interface WeatherRepository extends PagingAndSortingRepository<Weather, Integer> {

    boolean existsByCityNameAndDateCreate(CityName cityName, OffsetDateTime offsetDateTime);

    @Query(value = "" +
            "SELECT * " +
            "FROM weather " +
            "WHERE date_create IN (SELECT MAX(date_create) md " +
            "                      FROM weather " +
            "                      GROUP BY city_id, DATE(date_create) " +
            "                      ORDER BY md DESC) " +
                "AND DATE_TRUNC('day', date_create) = :dateTime " +
                "AND city_id = :cityId " +
            "ORDER BY date_create DESC",
            nativeQuery = true)
    Optional<Weather> findByCityId(Integer cityId, LocalDate dateTime);

    @Query(value = "" +
            "SELECT w.* " +
            "FROM weather w " +
            "WHERE w.date_create " +
                "IN (SELECT MAX(weather.date_create) AS md " +
            "FROM weather " +
            "WHERE DATE_TRUNC('day', weather.date_create) = :dateTime " +
            "GROUP BY weather.city_id " +
            "ORDER BY weather.city_id)",
            nativeQuery = true)
    List<Weather> findAllByDateCreate(LocalDate dateTime);

    @Query(value = "" +
            "SELECT * " +
            "FROM weather " +
            "WHERE date_create IN (SELECT MAX(date_create) md " +
            "                      FROM weather " +
            "                      GROUP BY city_id, DATE(date_create) " +
            "                      ORDER BY md DESC) " +
                "AND DATE_TRUNC('day', date_create) BETWEEN :start AND :end " +
            "ORDER BY date_create DESC",
            nativeQuery = true)
    List<Weather> findAllBetween(LocalDate start, LocalDate end);

    @Query(value = "" +
            "SELECT * " +
            "FROM weather " +
            "WHERE date_create IN (SELECT MAX(date_create) md " +
            "                      FROM weather " +
            "                      GROUP BY city_id, DATE(date_create) " +
            "                      ORDER BY md DESC) " +
                "AND DATE_TRUNC('day', date_create) BETWEEN :start AND :end " +
                "AND city_id = :cityId " +
            "ORDER BY date_create DESC",
            nativeQuery = true)
    List<Weather> findAllByIdAndBetween(Integer cityId, LocalDate start, LocalDate end);
}
