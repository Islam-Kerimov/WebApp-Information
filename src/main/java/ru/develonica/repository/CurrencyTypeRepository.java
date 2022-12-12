package ru.develonica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.develonica.model.entity.CurrencyType;

import java.util.Optional;

/**
 * Запросы в БД о валютах.
 */
public interface CurrencyTypeRepository extends JpaRepository<CurrencyType, Integer> {

    Optional<CurrencyType> findByNumCode(String numCode);
}
