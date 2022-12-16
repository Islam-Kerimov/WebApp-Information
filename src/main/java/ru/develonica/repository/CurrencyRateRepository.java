package ru.develonica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.develonica.model.entity.CurrencyRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Запросы в БД о курсах валют.
 */
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Integer> {

    boolean existsByDate(LocalDate date);

    @Modifying
    @Query(value = "" +
            "INSERT INTO currency_rate(currency_id, value, date) " +
            "VALUES (:id, :value, :localDate)",
            nativeQuery = true)
    @Transactional
    void saveBy(Integer id, Double value, LocalDate localDate);

    List<CurrencyRate> findAllByDate(LocalDate date);

    List<CurrencyRate> findAllByDateBetween(LocalDate dateFrom, LocalDate dateTo);

    @Query(value = "" +
            "SELECT * " +
            "FROM currency_rate cr JOIN currency_type ct " +
                "ON ct.currency_id = cr.currency_id " +
            "WHERE ct.num_code = :numCode " +
                "AND cr.date BETWEEN :dateF AND :dateT",
            nativeQuery = true)
    List<CurrencyRate> findAllByNumCodeAndBetween(String numCode, LocalDate dateF, LocalDate dateT);

    @Query(value = "" +
            "SELECT cr.* " +
            "FROM currency_rate cr JOIN currency_type ct " +
                "ON ct.currency_id = cr.currency_id " +
            "WHERE ct.num_code = :numCode " +
                "AND cr.date = :date",
            nativeQuery = true)
    Optional<CurrencyRate> findByNumCodeAndDate(String numCode, LocalDate date);

    @Query(value = "" +
            "SELECT c.date " +
            "FROM currency_rate c " +
            "ORDER BY c.date DESC " +
            "LIMIT 1",
            nativeQuery = true)
    LocalDate findLastDate();

    @Query(value = "" +
            "SELECT cr.date " +
            "FROM currency_rate cr JOIN currency_type ct " +
                "ON ct.currency_id = cr.currency_id " +
            "WHERE ct.num_code = :numCode " +
            "ORDER BY cr.date DESC " +
            "LIMIT 1",
            nativeQuery = true)
    LocalDate findLastDateBy(String numCode);
}
