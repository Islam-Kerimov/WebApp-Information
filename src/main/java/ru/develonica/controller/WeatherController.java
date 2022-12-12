package ru.develonica.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.develonica.exception.IncorrectData;
import ru.develonica.model.dto.WeatherDto;
import ru.develonica.service.WeatherService;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping(value = "/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /** Получение списка информации о погоде по всем городам за конкретную дату. */
    @GetMapping
    public List<WeatherDto> getAllWeather(@RequestParam(required = false) LocalDate date) {
        return weatherService.getAllWeather(date);
    }

    /** Получение списка информации о погоде по всем городам за интервал дат. */
    @GetMapping("/period")
    public List<WeatherDto> getAllWeatherBetween(@RequestParam LocalDate dateF,
                                                 @RequestParam LocalDate dateT) {
        return weatherService.getAllWeatherBetween(dateF, dateT);
    }

    /** Получение информации о погоде города по ID за конкретную дату. */
    @GetMapping("/{id}")
    public WeatherDto getWeatherById(@PathVariable Integer id,
                                     @RequestParam(required = false) LocalDate date) {
        return weatherService.getWeatherById(id, date);
    }

    /** Получение информации о погоде города по ID за интервал дат. */
    @GetMapping("/period/{id}")
    public List<WeatherDto> getWeatherByIdBetween(@PathVariable Integer id,
                                                  @RequestParam LocalDate dateF,
                                                  @RequestParam LocalDate dateT) {
        return weatherService.getWeatherByIdBetween(id, dateF, dateT);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleException(Exception exception) {
        IncorrectData data = new IncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, NOT_FOUND);
    }
}
