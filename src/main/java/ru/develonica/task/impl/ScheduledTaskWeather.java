package ru.develonica.task.impl;

import ru.develonica.model.entity.TaskProperties;
import ru.develonica.service.WeatherParserService;
import ru.develonica.task.ScheduledTask;
import ru.develonica.task.domain.WeatherOptions;

import java.io.IOException;

/**
 * Задача добавления данных погоды в базу.
 */
public class ScheduledTaskWeather extends ScheduledTask<WeatherOptions> {

    private final WeatherParserService weatherParserService;

    public ScheduledTaskWeather(TaskProperties taskProperties,
                                Class<WeatherOptions> clazz,
                                WeatherParserService weatherParserService) {
        super(taskProperties, clazz);
        this.weatherParserService = weatherParserService;
    }

    @Override
    protected void process() throws IOException {
        WeatherOptions options = getOptions();
        weatherParserService.addWeather(options.getCityId());
    }
}
