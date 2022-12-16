package ru.develonica.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import ru.develonica.model.entity.TaskProperties;
import ru.develonica.service.CurrencyParserService;
import ru.develonica.service.NewsParserService;
import ru.develonica.service.TaskPropertiesService;
import ru.develonica.service.WeatherParserService;
import ru.develonica.task.ScheduledTask;
import ru.develonica.task.domain.NewsOptions;
import ru.develonica.task.domain.WeatherOptions;
import ru.develonica.task.impl.ScheduledTaskCurrency;
import ru.develonica.task.impl.ScheduledTaskNews;
import ru.develonica.task.impl.ScheduledTaskWeather;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Настройка сервиса планирования задач.
 */
@Configuration
public class ApplicationConfiguration {

    private final TaskPropertiesService taskPropertiesService;

    private final TaskScheduler taskScheduler;

    private final CurrencyParserService currencyParserService;

    private final NewsParserService newsParserService;

    private final WeatherParserService weatherParserService;

    public ApplicationConfiguration(TaskPropertiesService taskPropertiesService,
                                    TaskScheduler taskScheduler,
                                    CurrencyParserService currencyParserService,
                                    NewsParserService newsParserService,
                                    WeatherParserService weatherParserService) {
        this.taskPropertiesService = taskPropertiesService;
        this.taskScheduler = taskScheduler;
        this.currencyParserService = currencyParserService;
        this.newsParserService = newsParserService;
        this.weatherParserService = weatherParserService;
    }

    /**
     * Инициализация всех задач.
     */
    @PostConstruct
    public void createTasks() {
        List<TaskProperties> tasks = taskPropertiesService.getTaskProperties();

        for (TaskProperties taskProperties : tasks) {
            ScheduledTask<?> scheduledTask = null;

            switch (taskProperties.getTypeObject()) {
                case NEWS -> scheduledTask = new ScheduledTaskNews(
                        taskProperties, NewsOptions.class, newsParserService);
                case WEATHER -> scheduledTask = new ScheduledTaskWeather(
                        taskProperties, WeatherOptions.class, weatherParserService);
                case CURRENCY -> scheduledTask = new ScheduledTaskCurrency(
                        taskProperties, null, currencyParserService);
            }

            taskScheduler.schedule(scheduledTask, new CronTrigger(taskProperties.getCronExpression()));
        }
    }
}
