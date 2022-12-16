package ru.develonica.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.develonica.model.data.ValCurs;
import ru.develonica.model.data.Valute;
import ru.develonica.model.entity.CurrencyType;
import ru.develonica.parser.XmlParser;
import ru.develonica.repository.CurrencyRateRepository;
import ru.develonica.repository.CurrencyTypeRepository;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.lang.Double.valueOf;
import static java.lang.Math.log10;
import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * Сервис отвечающий за занесения данных курса валют в базу.
 */
@Getter
@Setter
@Service
@Slf4j
public class CurrencyParserService {

    private static final String PATTERN_DATE = "dd.MM.yyyy";

    private final CurrencyRateRepository currencyRateRepository;

    private final CurrencyTypeRepository currencyTypeRepository;

    private final XmlParser<ValCurs> parser;

    @Value("${currency.url}")
    private String url;

    public CurrencyParserService(CurrencyRateRepository currencyRateRepository,
                                 CurrencyTypeRepository currencyTypeRepository) {
        this.currencyRateRepository = currencyRateRepository;
        this.currencyTypeRepository = currencyTypeRepository;
        this.parser = new XmlParser<>();
    }

    /**
     * Сохранение в БД данных о курсе валют.
     *
     * @throws MalformedURLException при ошибке работы с URL-адреса.
     * @throws JAXBException         при работе с парсером.
     */
    public void addCurrencyRates() throws MalformedURLException, JAXBException {
        ValCurs valCurs = (ValCurs) parser.parse(url, ValCurs.class);
        LocalDate date = parse(valCurs.getDate(), ofPattern(PATTERN_DATE));

        if (currencyRateRepository.existsByDate(date)) {
            log.warn("The exchange rate for the current date has already been entered");
            return;
        }

        if (currencyTypeRepository.count() == 0) {
            addAllTypes(valCurs.getValutes());
        }

        addAllRates(valCurs.getValutes(), date);
    }

    private void addAllTypes(List<Valute> valutes) {
        List<CurrencyType> types = valutes.stream()
                .map(type -> {
                    CurrencyType currencyType = new CurrencyType();
                    currencyType.setNumCode(type.getNumCode());
                    currencyType.setCharCode(type.getCharCode());
                    currencyType.setLogNominal((int) log10(type.getNominal()));
                    currencyType.setName(type.getName());
                    return currencyType;
                })
                .toList();

        currencyTypeRepository.saveAll(types);
    }

    public void addAllRates(List<Valute> valutes, LocalDate date) {
        for (Valute valute : valutes) {
            Optional<CurrencyType> type = currencyTypeRepository.findByNumCode(valute.getNumCode());
            currencyRateRepository.saveBy(
                    type.get().getId(),
                    valueOf(valute.getValue().replace(",", ".")),
                    date);
        }
    }
}
