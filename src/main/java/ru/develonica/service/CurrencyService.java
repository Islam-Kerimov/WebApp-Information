package ru.develonica.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.develonica.exception.EntityNotFoundException;
import ru.develonica.exception.ParameterNotFoundException;
import ru.develonica.model.dto.CurrencyRateDto;
import ru.develonica.model.dto.CurrencyTypeDto;
import ru.develonica.model.entity.CurrencyRate;
import ru.develonica.model.entity.CurrencyType;
import ru.develonica.model.mapper.CurrencyMapper;
import ru.develonica.repository.CurrencyRateRepository;
import ru.develonica.repository.CurrencyTypeRepository;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

/**
 * Сервис отвечающий за работу с информацией о курсе валют.
 */
@Service
public class CurrencyService {

    private static final String MESSAGE_FAIL_NUM_CODE = "Currency type with numCode: '%s' not found";

    private final CurrencyParserService currencyParserService;

    private final CurrencyRateRepository currencyRateRepository;

    private final CurrencyTypeRepository currencyTypeRepository;

    private final CurrencyMapper currencyMapper;

    public CurrencyService(CurrencyParserService currencyParserService,
                           CurrencyRateRepository currencyRateRepository,
                           CurrencyTypeRepository currencyTypeRepository,
                           CurrencyMapper currencyMapper) {
        this.currencyParserService = currencyParserService;
        this.currencyRateRepository = currencyRateRepository;
        this.currencyTypeRepository = currencyTypeRepository;
        this.currencyMapper = currencyMapper;
    }

    /**
     * Добавление в БД информацию о курсе валют.
     *
     * @throws MalformedURLException при ошибке работы с URL-адреса.
     * @throws JAXBException         при работе с парсером.
     */
    public void addCurrencyRates() throws MalformedURLException, JAXBException {
        currencyParserService.addCurrenciesAndExchangeRates();
    }

    /**
     * Получения всех курсов валют за конкретную дату.
     *
     * @param date день
     * @return список курса валют.
     */
    @Transactional
    public List<CurrencyRateDto> getAllCurrencies(LocalDate date) {
        if (date == null) {
            date = currencyRateRepository.findLastDate();
        }

        List<CurrencyRate> currencyRates = currencyRateRepository.findAllByDate(date);

        return getCurrencyRateDtos(currencyRates);
    }

    /**
     * Получения всех курсов валют за интервал дат.
     *
     * @param dateF дата начала
     * @param dateT дата окончания
     * @return список курса валют
     */
    @Transactional
    public List<CurrencyRateDto> getAllCurrenciesBetween(LocalDate dateF, LocalDate dateT) {
        List<CurrencyRate> currencyRates = currencyRateRepository.findAllByDateBetween(dateF, dateT);

        return getCurrencyRateDtos(currencyRates);
    }

    /**
     * Получение выбранного курса валюты за конкретную дату.
     *
     * @param numCode код валюты для получения
     * @param date    день
     * @return курс выбранной валюты.
     */
    public CurrencyRateDto getCurrency(String numCode, LocalDate date) {
        if (date == null) {
            date = currencyRateRepository.findLastDateBy(numCode);
        }

        Optional<CurrencyType> currencyType = currencyTypeRepository.findByNumCode(numCode);
        if (currencyType.isEmpty()) {
            throw new ParameterNotFoundException(format(MESSAGE_FAIL_NUM_CODE, numCode));
        }

        Optional<CurrencyRate> currencyRate =
                currencyRateRepository.findByCurrencyTypeAndDate(currencyType.get(), date);
        if (currencyRate.isEmpty()) {
            throw new EntityNotFoundException(format(MESSAGE_FAIL_NUM_CODE, numCode));
        }

        CurrencyRateDto currencyRateDto = currencyMapper.currencyRateEntityToDto(currencyRate.get());
        currencyRateDto.setCurrencyTypeDto(currencyMapper.currencyTypeEntityToDto(currencyType.get()));
        return currencyRateDto;
    }

    /**
     * Получения курса выбранной валюты за интервал дат.
     *
     * @param numCode код валюты для получения
     * @param dateF   дата начала
     * @param dateT   дата окончания
     * @return список курса выбранной валюты за заданный интервал времени.
     */
    @Transactional
    public List<CurrencyRateDto> getCurrencyBetween(String numCode, LocalDate dateF, LocalDate dateT) {
        Optional<CurrencyType> currencyType = currencyTypeRepository.findByNumCode(numCode);
        if (currencyType.isEmpty()) {
            throw new ParameterNotFoundException(format(MESSAGE_FAIL_NUM_CODE, numCode));
        }

        List<CurrencyRate> currencyRates =
                currencyRateRepository.findAllByCurrencyTypeAndDateBetween(currencyType.get(), dateF, dateT);

        return getCurrencyRateDtos(currencyRates);
    }

    private List<CurrencyRateDto> getCurrencyRateDtos(List<CurrencyRate> currencyRates) {
        return currencyRates.stream()
                .map(rate -> {
                    CurrencyTypeDto currencyTypeDto = currencyMapper.currencyTypeEntityToDto(rate.getCurrencyType());
                    CurrencyRateDto currencyRateDto = currencyMapper.currencyRateEntityToDto(rate);
                    currencyRateDto.setCurrencyTypeDto(currencyTypeDto);
                    return currencyRateDto;
                })
                .toList();
    }
}
