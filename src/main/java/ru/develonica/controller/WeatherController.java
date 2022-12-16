package ru.develonica.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.develonica.exception.EntityNotFoundException;
import ru.develonica.exception.IncorrectData;
import ru.develonica.model.dto.WeatherDto;
import ru.develonica.model.entity.Weather;
import ru.develonica.model.mapper.WeatherMapper;
import ru.develonica.service.WeatherService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping(value = "/weather")
public class WeatherController {

    private static final String MESSAGE_FAIL_DATE = "Weather for date '%s' not found";

    private final WeatherService weatherService;

    private final WeatherMapper weatherMapper;

    public WeatherController(WeatherService weatherService,
                             WeatherMapper weatherMapper) {
        this.weatherService = weatherService;
        this.weatherMapper = weatherMapper;
    }

    /** Получение списка информации о погоде по всем городам за конкретную дату. */
    @GetMapping
    public List<WeatherDto> getAllWeather(@RequestParam(required = false) LocalDate date) {
        List<Weather> weather = weatherService.getAllWeather(date);
        return weatherMapper.entityWeatherListToDtoList(weather);
    }

    /** Получение списка информации о погоде по всем городам за интервал дат. */
    @GetMapping("/period")
    public List<WeatherDto> getAllWeatherBetween(@RequestParam LocalDate dateF,
                                                 @RequestParam LocalDate dateT) {
        List<Weather> weather = weatherService.getAllWeatherBetween(dateF, dateT);
        return weatherMapper.entityWeatherListToDtoList(weather);
    }

    /** Получение информации о погоде города по ID за конкретную дату. */
    @GetMapping("/{id}")
    public WeatherDto getWeatherById(@PathVariable Integer id,
                                     @RequestParam(required = false) LocalDate date) {
        Optional<Weather> weatherById = weatherService.getWeatherById(id, date);
        return weatherMapper.entityWeatherToDto(weatherById
                .orElseThrow(() -> new EntityNotFoundException(format(MESSAGE_FAIL_DATE, date))));
    }

    /** Получение информации о погоде города по ID за интервал дат. */
    @GetMapping("/period/{id}")
    public List<WeatherDto> getWeatherByIdBetween(@PathVariable Integer id,
                                                  @RequestParam LocalDate dateF,
                                                  @RequestParam LocalDate dateT) {
        List<Weather> weather = weatherService.getWeatherByIdBetween(id, dateF, dateT);
        return weatherMapper.entityWeatherListToDtoList(weather);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleException(Exception exception) {
        IncorrectData data = new IncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, NOT_FOUND);
    }
}
