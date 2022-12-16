package ru.develonica.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.develonica.model.data.Item;
import ru.develonica.model.data.Rss;
import ru.develonica.model.entity.NewsBody;
import ru.develonica.model.entity.NewsSource;
import ru.develonica.parser.XmlParser;
import ru.develonica.repository.NewsBodyRepository;
import ru.develonica.repository.NewsSourceRepository;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Optional;

import static java.time.Instant.ofEpochMilli;
import static java.time.OffsetDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.util.Locale.ENGLISH;

/**
 * Сервис отвечающий за занесения данных новостей в базу.
 */
@Getter
@Setter
@Service
@Slf4j
public class NewsParserService {

    private static final String PATTERN_DATE = "E, dd MMM yyyy HH:mm:ss Z";

    private final NewsBodyRepository newsBodyRepository;

    private final NewsSourceRepository newsSourceRepository;

    private final XmlParser<Rss> parser;

    public NewsParserService(NewsBodyRepository newsBodyRepository,
                             NewsSourceRepository newsSourceRepository) {
        this.newsBodyRepository = newsBodyRepository;
        this.newsSourceRepository = newsSourceRepository;
        this.parser = new XmlParser<>();
    }

    /**
     * Сохранение в БД информации о новых новостях.
     *
     * @param url адрес rss страницы с новостями.
     * @throws MalformedURLException при ошибке работы с URL-адреса.
     * @throws JAXBException         при работе с парсером.
     * @throws ParseException        при форматировании строки в дату.
     */
    public void addNews(String url) throws MalformedURLException, JAXBException, ParseException {
        Rss rss = (Rss) parser.parse(url, Rss.class);

        NewsSource newsSource = new NewsSource(rss.getChannel().getNewsName().replaceAll("\"", "\\'"), url);

        if (!newsSourceRepository.existsByName(newsSource.getName())) {
            newsSourceRepository.save(newsSource);
        } else {
            log.warn("This news source already exists");
        }

        addNewsBodies(rss);
    }

    private void addNewsBodies(Rss rss)
            throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_DATE, ENGLISH);

        Optional<NewsBody> lastSource = newsBodyRepository
                .findLastBySource(rss.getChannel().getNewsName().replaceAll("\"", "\\'"));

        for (Item item : rss.getChannel().getItems()) {
            OffsetDateTime publicDate = ofInstant(
                    ofEpochMilli(format.parse(item.getPublicDate()).getTime()),
                    systemDefault());

            if (lastSource.isEmpty() || publicDate.isAfter(lastSource.get().getPublicDate())) {
                newsBodyRepository.saveBySource(
                        rss.getChannel().getNewsName().replaceAll("\"", "\\'"),
                        item.getTitle(),
                        item.getCategory(),
                        item.getDescription(),
                        item.getNewsUrl(),
                        publicDate);
            }
        }
    }
}
