package ru.develonica.task.impl;

import ru.develonica.model.entity.TaskProperties;
import ru.develonica.service.CurrencyService;
import ru.develonica.task.ScheduledTask;
import ru.develonica.task.domain.Options;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;

/**
 * Задача добавления данных курса валют в базу.
 */
public class ScheduledTaskCurrency extends ScheduledTask<Options> {

    private final CurrencyService currencyService;

    public ScheduledTaskCurrency(TaskProperties taskProperties,
                                 Class<Options> clazz,
                                 CurrencyService currencyService) {
        super(taskProperties, clazz);
        this.currencyService = currencyService;
    }

    @Override
    protected void process() throws MalformedURLException, JAXBException {
        currencyService.addCurrencyRates();
    }
}
