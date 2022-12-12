package ru.develonica.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.develonica.exception.EntityNotFoundException;
import ru.develonica.model.dto.CityNameDto;
import ru.develonica.model.dto.WeatherDto;
import ru.develonica.model.entity.CityName;
import ru.develonica.model.entity.Weather;
import ru.develonica.model.mapper.WeatherMapper;
import ru.develonica.repository.CityNameRepository;
import ru.develonica.repository.WeatherRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.time.LocalDate.now;
import static java.time.LocalTime.MAX;
import static java.time.OffsetDateTime.of;
import static java.time.ZoneOffset.UTC;


/**
 * Сервис отвечающий за работу с информацией о погоде.
 */
@Service
public class WeatherService {

    private static final String MESSAGE_FAIL_DATE = "Weather for date '%s' not found";
    private static final String MESSAGE_FAIL_ID = "City with id '%s' not found";
    private final WeatherParserService weatherParserService;

    private final WeatherRepository weatherRepository;

    private final CityNameRepository cityNameRepository;

    private final WeatherMapper weatherMapper;

    public WeatherService(WeatherParserService weatherParserService,
                          WeatherRepository weatherRepository,
                          CityNameRepository cityNameRepository,
                          WeatherMapper weatherMapper) {
        this.weatherParserService = weatherParserService;
        this.weatherRepository = weatherRepository;
        this.cityNameRepository = cityNameRepository;
        this.weatherMapper = weatherMapper;
    }

    /**
     * Сохранение в БД информации о погоде.
     *
     * @param cityId идентификатор города
     * @throws IOException при работе с маппером.
     */
    public void addWeather(String cityId) throws IOException {
        weatherParserService.addWeather(cityId);
    }

    /**
     * Получение информации о погоде по всем городам за конкретную дату.
     *
     * @param date день
     * @return список информации о погоде.
     */
    @Transactional
    public List<WeatherDto> getAllWeather(LocalDate date) {
        if (date == null) {
            date = now();
        }

        List<WeatherDto> weatherList = new ArrayList<>();
        for (CityName cityName : cityNameRepository.findAll()) {
            WeatherDto weatherById = getWeatherById(cityName.getId(), date);
            if (weatherById != null) {
                weatherList.add(weatherById);
            }
        }

        return weatherList;
    }

    /**
     * Получение информации о погоде по всем городам за интервал дат.
     *
     * @param dateF дата начала
     * @param dateT дата окончания
     * @return список информации о погоде за заданный период
     */
    @Transactional
    public List<WeatherDto> getAllWeatherBetween(LocalDate dateF, LocalDate dateT) {
        List<WeatherDto> weatherList = new ArrayList<>();

        for (LocalDate date = dateF; date.isBefore(dateT.plusDays(1)); date = date.plusDays(1)) {
            List<WeatherDto> weather = getAllWeather(date);
            if (date.isAfter(now().plusDays(1))) {
                break;
            }
            if (weather.size() != 0) {
                weatherList.addAll(weather);
            }
        }

        return weatherList;
    }

    /**
     * Получение информации о погоде определенного города за конкретную дату.
     *
     * @param id   идентификатор города
     * @param date день
     * @return информацию о погоде по выбранному городу.
     */
    @Transactional
    public WeatherDto getWeatherById(Integer id, LocalDate date) {
        if (date == null) {
            date = now();
        }

        OffsetDateTime start = of(date.atStartOfDay(), UTC);
        OffsetDateTime end = of(date.atTime(MAX), UTC);

        Optional<CityName> cityName = cityNameRepository.findById(id);

        if (cityName.isPresent()) {
            Optional<Weather> weather = weatherRepository.findBy(cityName.get().getName(), start, end);
            if (weather.isPresent()) {
                CityNameDto cityNameDto = weatherMapper.cityNameEntityToDto(cityName.get());
                WeatherDto weatherDto = weatherMapper.weatherEntityToDto(weather.get());
                weatherDto.setCityNameDto(cityNameDto);
                return weatherDto;
            }
            throw new EntityNotFoundException(format(MESSAGE_FAIL_DATE, date));
        }
        throw new EntityNotFoundException(format(MESSAGE_FAIL_ID, id));
    }

    /**
     * Получение информации о погоде определенного города за интервал дат.
     *
     * @param id    идентификатор города
     * @param dateF дата начала
     * @param dateT дата окончания
     * @return список информации о погоде по выбранному городу за заданный период.
     */
    @Transactional
    public List<WeatherDto> getWeatherByIdBetween(Integer id, LocalDate dateF, LocalDate dateT) {
        List<WeatherDto> weatherList = new ArrayList<>();

        for (LocalDate date = dateF; date.isBefore(dateT.plusDays(1)); date = date.plusDays(1)) {
            WeatherDto weather = getWeatherById(id, date);
            if (date.isAfter(now().plusDays(1))) {
                break;
            }
            if (weather != null) {
                weatherList.add(weather);
            }
        }
        return weatherList;
    }
}
