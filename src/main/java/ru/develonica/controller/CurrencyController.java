package ru.develonica.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.develonica.exception.IncorrectData;
import ru.develonica.exception.ParameterNotFoundException;
import ru.develonica.model.dto.CurrencyRateDto;
import ru.develonica.model.entity.CurrencyRate;
import ru.develonica.model.mapper.CurrencyRateMapper;
import ru.develonica.service.CurrencyService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping(value = "/currencies")
public class CurrencyController {

    private static final String MESSAGE_FAIL_NUM_CODE = "Currency type with numCode: '%s' not found";

    private final CurrencyRateMapper currencyRateMapper;

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyRateMapper currencyRateMapper,
                              CurrencyService currencyService) {
        this.currencyRateMapper = currencyRateMapper;
        this.currencyService = currencyService;
    }

    /** Получения списка всех курсов валют за конкретную дату. */
    @GetMapping
    public List<CurrencyRateDto> getAllCurrencies(@RequestParam(required = false) LocalDate date) {
        List<CurrencyRate> currencyRates = currencyService.getAllCurrencies(date);
        return currencyRateMapper.entityRateListToDtoList(currencyRates);
    }

    /** Получения списка всех курсов валют за интервал дат. */
    @GetMapping("/period")
    public List<CurrencyRateDto> getAllCurrenciesBetween(@RequestParam LocalDate dateF,
                                                         @RequestParam LocalDate dateT) {
        List<CurrencyRate> currencyRates = currencyService.getAllCurrenciesBetween(dateF, dateT);
        return currencyRateMapper.entityRateListToDtoList(currencyRates);
    }

    /** Получение курса валюты по его коду за конкретную дату. */
    @GetMapping("/{numCode}")
    public CurrencyRateDto getCurrencyRate(@PathVariable String numCode,
                                           @RequestParam(required = false) LocalDate date) {
        Optional<CurrencyRate> currency = currencyService.getCurrency(numCode, date);
        return currencyRateMapper.entityRateToDto(currency
                .orElseThrow(() -> new ParameterNotFoundException(format(MESSAGE_FAIL_NUM_CODE, numCode))));
    }

    /** Получение курса валюты по его коду за интервал дат. */
    @GetMapping("/period/{numCode}")
    public List<CurrencyRateDto> getCurrencyRateBetween(@PathVariable String numCode,
                                                        @RequestParam LocalDate dateF,
                                                        @RequestParam LocalDate dateT) {
        List<CurrencyRate> currencyRates = currencyService.getCurrencyBetween(numCode, dateF, dateT);
        return currencyRateMapper.entityRateListToDtoList(currencyRates);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleException(Exception exception) {
        IncorrectData data = new IncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, NOT_FOUND);
    }
}
