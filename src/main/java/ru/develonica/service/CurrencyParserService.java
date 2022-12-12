package ru.develonica.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.develonica.model.data.ValCurs;
import ru.develonica.model.entity.CurrencyRate;
import ru.develonica.model.entity.CurrencyType;
import ru.develonica.parser.XmlParser;
import ru.develonica.repository.CurrencyRateRepository;
import ru.develonica.repository.CurrencyTypeRepository;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.Double.parseDouble;
import static java.lang.Math.log10;
import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Comparator.comparing;

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
    @Transactional
    public void addCurrenciesAndExchangeRates() throws MalformedURLException, JAXBException {
        ValCurs valCurs = (ValCurs) parser.parse(url, ValCurs.class);
        LocalDate date = parse(valCurs.getDate(), ofPattern(PATTERN_DATE));

        if (currencyRateRepository.existsByDate(date)) {
            log.warn("The exchange rate for the current date has already been entered");
            return;
        }

        List<CurrencyRate> currencyRates = getCurrencyRates(valCurs, date);

        if (currencyTypeRepository.count() == 0) {
            Set<CurrencyType> currencyType = new TreeSet<>(comparing(CurrencyType::getCharCode));
            currencyRates.forEach(rate -> currencyType.add(rate.getCurrencyType()));

            currencyTypeRepository.saveAll(currencyType);
        }

        currencyRates.forEach(rate -> {
            Optional<CurrencyType> rightType = currencyTypeRepository.findByNumCode(rate.getCurrencyType().getNumCode());
            rightType.ifPresent(rate::setCurrencyType);

            currencyRateRepository.save(rate);
        });
    }

    private List<CurrencyRate> getCurrencyRates(ValCurs valCurs, LocalDate date) {
        List<CurrencyRate> currencyRates = new ArrayList<>();

        valCurs.getValutes().forEach(val -> currencyRates.add(new CurrencyRate(
                CurrencyType.builder()
                        .numCode(val.getNumCode())
                        .charCode(val.getCharCode())
                        .logNominal((int) log10(val.getNominal()))
                        .name(val.getName())
                        .build(),
                parseDouble(val.getValue().replace(",", ".")),
                date)));
        return currencyRates;
    }
}
