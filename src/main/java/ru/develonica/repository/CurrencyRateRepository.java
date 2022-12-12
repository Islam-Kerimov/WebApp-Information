package ru.develonica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.develonica.model.entity.CurrencyRate;
import ru.develonica.model.entity.CurrencyType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Запросы в БД о курсах валют.
 */
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Integer> {

    boolean existsByDate(LocalDate date);

    List<CurrencyRate> findAllByDate(LocalDate date);

    List<CurrencyRate> findAllByDateBetween(LocalDate dateFrom, LocalDate dateTo);

    Optional<CurrencyRate> findByCurrencyTypeAndDate(CurrencyType currencyType, LocalDate date);

    List<CurrencyRate> findAllByCurrencyTypeAndDateBetween(
            CurrencyType currencyType, LocalDate dateFrom, LocalDate dateTo);

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
