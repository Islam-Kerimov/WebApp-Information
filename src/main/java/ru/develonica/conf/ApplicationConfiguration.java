package ru.develonica.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import ru.develonica.model.entity.TaskProperties;
import ru.develonica.service.CurrencyService;
import ru.develonica.service.NewsService;
import ru.develonica.service.TaskPropertiesService;
import ru.develonica.service.WeatherService;
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

    private final CurrencyService currencyService;

    private final NewsService newsService;

    private final WeatherService weatherService;

    public ApplicationConfiguration(TaskPropertiesService taskPropertiesService,
                                    TaskScheduler taskScheduler,
                                    CurrencyService currencyService,
                                    NewsService newsService,
                                    WeatherService weatherService) {
        this.taskPropertiesService = taskPropertiesService;
        this.taskScheduler = taskScheduler;
        this.currencyService = currencyService;
        this.newsService = newsService;
        this.weatherService = weatherService;
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
                        taskProperties, NewsOptions.class, newsService);
                case WEATHER -> scheduledTask = new ScheduledTaskWeather(
                        taskProperties, WeatherOptions.class, weatherService);
                case CURRENCY -> scheduledTask = new ScheduledTaskCurrency(
                        taskProperties, null, currencyService);
            }

            taskScheduler.schedule(scheduledTask, new CronTrigger(taskProperties.getCronExpression()));
        }
    }
}
