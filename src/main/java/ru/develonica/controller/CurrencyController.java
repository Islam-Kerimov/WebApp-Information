package ru.develonica.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.develonica.exception.IncorrectData;
import ru.develonica.model.dto.CurrencyRateDto;
import ru.develonica.service.CurrencyService;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping(value = "/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /** Получения списка всех курсов валют за конкретную дату. */
    @GetMapping
    public List<CurrencyRateDto> getAllCurrencies(@RequestParam(required = false) LocalDate date) {
        return currencyService.getAllCurrencies(date);

    }

    /** Получения списка всех курсов валют за интервал дат. */
    @GetMapping("/period")
    public List<CurrencyRateDto> getAllCurrenciesBetween(@RequestParam LocalDate dateF,
                                                         @RequestParam LocalDate dateT) {
        return currencyService.getAllCurrenciesBetween(dateF, dateT);
    }

    /** Получение курса валюты по его коду за конкретную дату. */
    @GetMapping("/{numCode}")
    public CurrencyRateDto getCurrencyRate(@PathVariable String numCode,
                                           @RequestParam(required = false) LocalDate date) {
        return currencyService.getCurrency(numCode, date);
    }

    /** Получение курса валюты по его коду за интервал дат. */
    @GetMapping("/period/{numCode}")
    public List<CurrencyRateDto> getCurrencyRateBetween(@PathVariable String numCode,
                                                        @RequestParam LocalDate dateF,
                                                        @RequestParam LocalDate dateT) {
        return currencyService.getCurrencyBetween(numCode, dateF, dateT);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleException(Exception exception) {
        IncorrectData data = new IncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, NOT_FOUND);
    }
}
