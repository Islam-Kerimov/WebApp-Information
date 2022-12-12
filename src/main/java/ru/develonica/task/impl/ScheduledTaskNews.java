package ru.develonica.task.impl;

import lombok.extern.slf4j.Slf4j;
import ru.develonica.model.entity.TaskProperties;
import ru.develonica.service.NewsService;
import ru.develonica.task.ScheduledTask;
import ru.develonica.task.domain.NewsOptions;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.text.ParseException;

/**
 * Задача добавления данных новостей в базу.
 */
@Slf4j
public class ScheduledTaskNews extends ScheduledTask<NewsOptions> {
    private final NewsService newsService;

    public ScheduledTaskNews(TaskProperties taskProperties,
                             Class<NewsOptions> clazz,
                             NewsService newsService) {
        super(taskProperties, clazz);
        this.newsService = newsService;
    }

    @Override
    protected void process() throws IOException, JAXBException {
        try {
            NewsOptions options = getOptions();
            newsService.addNews(options.getUrl());
        } catch (ParseException pe) {
            log.error(pe.getMessage());
        }
    }
}
