package ru.develonica.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.develonica.model.entity.CityName;
import ru.develonica.model.entity.Weather;
import ru.develonica.repository.CityNameRepository;
import ru.develonica.repository.WeatherRepository;

import java.io.IOException;
import java.net.URL;

import static java.lang.String.format;

/**
 * Сервис отвечающий за занесения данных погоды в базу.
 */
@Getter
@Setter
@Service
@Slf4j
public class WeatherParserService {

    private final WeatherRepository weatherRepository;

    private final CityNameRepository cityNameRepository;

    @Value("${weather.api-key}")
    private String apiKey;

    @Value("${weather.url}")
    private String url;

    public WeatherParserService(WeatherRepository weatherRepository, CityNameRepository cityNameRepository) {
        this.weatherRepository = weatherRepository;
        this.cityNameRepository = cityNameRepository;
    }

    /**
     * Сохранение в БД информации о погоде.
     *
     * @param cityId идентификатор города
     * @throws IOException при работе с маппером.
     */
    @Transactional
    public void addWeather(String cityId) throws IOException {
        URL weatherUrl = new URL(format(url, cityId, apiKey));
        ObjectMapper objectMapper = new ObjectMapper();

        CityName cityName = objectMapper.readValue(weatherUrl, CityName.class);
        Weather weather = objectMapper.readValue(weatherUrl, Weather.class);
        weather.setCityName(cityName);
        weather.setDateCreate(weather.getDt());

        if (!cityNameRepository.existsByName(cityName.getName())) {
            cityNameRepository.save(cityName);
        } else {
            log.warn("This city already exists");
        }

        if (!weatherRepository.existsByCityNameAndDateCreate(
                weather.getCityName(), weather.getDateCreate())) {
            weatherRepository.save(weather);
        } else {
            log.warn("Weather data already entered");
        }
    }
}
