package ru.develonica.task.impl;

import ru.develonica.model.entity.TaskProperties;
import ru.develonica.service.WeatherService;
import ru.develonica.task.ScheduledTask;
import ru.develonica.task.domain.WeatherOptions;

import java.io.IOException;

/**
 * Задача добавления данных погоды в базу.
 */
public class ScheduledTaskWeather extends ScheduledTask<WeatherOptions> {

    private final WeatherService weatherService;

    public ScheduledTaskWeather(TaskProperties taskProperties,
                                Class<WeatherOptions> clazz,
                                WeatherService weatherService) {
        super(taskProperties, clazz);
        this.weatherService = weatherService;
    }

    @Override
    protected void process() throws IOException {
        WeatherOptions options = getOptions();
        weatherService.addWeather(options.getCityId());
    }
}
