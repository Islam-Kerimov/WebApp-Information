package ru.develonica.task.impl;

import ru.develonica.model.entity.TaskProperties;
import ru.develonica.service.CurrencyParserService;
import ru.develonica.service.CurrencyService;
import ru.develonica.task.ScheduledTask;
import ru.develonica.task.domain.Options;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;

/**
 * Задача добавления данных курса валют в базу.
 */
public class ScheduledTaskCurrency extends ScheduledTask<Options> {

    private final CurrencyParserService currencyParserService;

    public ScheduledTaskCurrency(TaskProperties taskProperties,
                                 Class<Options> clazz,
                                 CurrencyParserService currencyParserService) {
        super(taskProperties, clazz);
        this.currencyParserService = currencyParserService;
    }

    @Override
    protected void process() throws MalformedURLException, JAXBException {
        currencyParserService.addCurrencyRates();
    }
}
