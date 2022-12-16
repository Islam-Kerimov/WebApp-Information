package ru.develonica.service;

import org.springframework.stereotype.Service;
import ru.develonica.model.entity.CurrencyRate;
import ru.develonica.repository.CurrencyRateRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Сервис отвечающий за работу с информацией о курсе валют.
 */
@Service
public class CurrencyService {

    private final CurrencyRateRepository currencyRateRepository;

    public CurrencyService(CurrencyRateRepository currencyRateRepository) {
        this.currencyRateRepository = currencyRateRepository;
    }

    /**
     * Получения всех курсов валют за конкретную дату.
     *
     * @param date день
     * @return список курса валют.
     */
    public List<CurrencyRate> getAllCurrencies(LocalDate date) {
        if (date == null) {
            date = currencyRateRepository.findLastDate();
        }

        return currencyRateRepository.findAllByDate(date);
    }

    /**
     * Получения всех курсов валют за интервал дат.
     *
     * @param dateF дата начала
     * @param dateT дата окончания
     * @return список курса валют
     */
    public List<CurrencyRate> getAllCurrenciesBetween(LocalDate dateF, LocalDate dateT) {
        return currencyRateRepository.findAllByDateBetween(dateF, dateT);
    }

    /**
     * Получение выбранного курса валюты за конкретную дату.
     *
     * @param numCode код валюты для получения
     * @param date    день
     * @return курс выбранной валюты.
     */
    public Optional<CurrencyRate> getCurrency(String numCode, LocalDate date) {
        if (date == null) {
            date = currencyRateRepository.findLastDateBy(numCode);
        }

        return currencyRateRepository.findByNumCodeAndDate(numCode, date);
    }

    /**
     * Получения курса выбранной валюты за интервал дат.
     *
     * @param numCode код валюты для получения
     * @param dateF   дата начала
     * @param dateT   дата окончания
     * @return список курса выбранной валюты за заданный интервал времени.
     */
    public List<CurrencyRate> getCurrencyBetween(String numCode, LocalDate dateF, LocalDate dateT) {
        return currencyRateRepository.findAllByNumCodeAndBetween(numCode, dateF, dateT);
    }
}
