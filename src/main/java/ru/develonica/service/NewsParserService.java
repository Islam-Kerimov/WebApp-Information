package ru.develonica.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.Instant.ofEpochMilli;
import static java.time.OffsetDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.util.Collections.reverse;
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
    @Transactional
    public void addNews(String url) throws MalformedURLException, JAXBException, ParseException {
        Rss rss = (Rss) parser.parse(url, Rss.class);

        NewsSource newsSource = new NewsSource(rss.getChannel().getNewsName().replaceAll("\"", "\\'"), url);

        if (!newsSourceRepository.existsByName(newsSource.getName())) {
            newsSourceRepository.save(newsSource);
        } else {
            log.warn("This news source already exists");
        }

        NewsSource sourceByName = newsSourceRepository.findByName(newsSource.getName());
        Optional<NewsBody> lastNews = newsBodyRepository.findByLast(sourceByName.getName());

        List<NewsBody> newsBodies = getNewsBodies(rss, sourceByName, lastNews);

        long countBefore = newsBodyRepository.count();
        newsBodyRepository.saveAll(newsBodies);
        long countAfter = newsBodyRepository.count();
        if (countAfter == countBefore) {
            log.warn("No new news yet");
        }
    }

    private List<NewsBody> getNewsBodies(Rss rss, NewsSource newsSource, Optional<NewsBody> lastNews)
            throws ParseException {
        List<NewsBody> newsBodies = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_DATE, ENGLISH);

        for (Item item : rss.getChannel().getItems()) {
            OffsetDateTime publicDate = ofInstant(
                    ofEpochMilli(format.parse(item.getPublicDate()).getTime()),
                    systemDefault());
            if (lastNews.isEmpty() || publicDate.isAfter(lastNews.get().getPublicDate())) {
                newsBodies.add(NewsBody.builder()
                        .newsSource(newsSource)
                        .title(item.getTitle())
                        .category(item.getCategory())
                        .description(item.getDescription())
                        .urlNews(item.getNewsUrl())
                        .publicDate(publicDate)
                        .build());
            } else {
                break;
            }
        }

        reverse(newsBodies);
        return newsBodies;
    }
}
