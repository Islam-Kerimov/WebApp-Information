package ru.develonica.service;

import org.springframework.stereotype.Service;
import ru.develonica.model.entity.Weather;
import ru.develonica.repository.WeatherRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;


/**
 * Сервис отвечающий за работу с информацией о погоде.
 */
@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    /**
     * Получение информации о погоде по всем городам за конкретную дату.
     *
     * @param date день
     * @return список информации о погоде.
     */
    public List<Weather> getAllWeather(LocalDate date) {
        if (date == null) {
            date = now();
        }

        return weatherRepository.findAllByDateCreate(date);
    }

    /**
     * Получение информации о погоде по всем городам за интервал дат.
     *
     * @param dateF дата начала
     * @param dateT дата окончания
     * @return список информации о погоде за заданный период
     */
    public List<Weather> getAllWeatherBetween(LocalDate dateF, LocalDate dateT) {
        return weatherRepository.findAllBetween(dateF, dateT);
    }

    /**
     * Получение информации о погоде определенного города за конкретную дату.
     *
     * @param id   идентификатор города
     * @param date день
     * @return информацию о погоде по выбранному городу.
     */
    public Optional<Weather> getWeatherById(Integer id, LocalDate date) {
        if (date == null) {
            date = now();
        }

        return weatherRepository.findByCityId(id, date);
    }

    /**
     * Получение информации о погоде определенного города за интервал дат.
     *
     * @param id    идентификатор города
     * @param dateF дата начала
     * @param dateT дата окончания
     * @return список информации о погоде по выбранному городу за заданный период.
     */
    public List<Weather> getWeatherByIdBetween(Integer id, LocalDate dateF, LocalDate dateT) {
        return weatherRepository.findAllByIdAndBetween(id, dateF, dateT);
    }
}
