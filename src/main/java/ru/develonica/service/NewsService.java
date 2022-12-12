package ru.develonica.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.develonica.model.dto.NewsDto;
import ru.develonica.model.entity.NewsBody;
import ru.develonica.model.entity.NewsSource;
import ru.develonica.model.mapper.NewsMapper;
import ru.develonica.repository.NewsBodyRepository;
import ru.develonica.repository.NewsSourceRepository;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static java.time.LocalDate.now;
import static java.time.LocalTime.MAX;
import static java.time.OffsetDateTime.of;
import static java.time.ZoneOffset.UTC;
import static java.util.Comparator.comparing;

/**
 * Сервис отвечающий за работу с информацией о новостях.
 */
@Service
public class NewsService {

    private final NewsParserService newsParserService;

    private final NewsBodyRepository newsBodyRepository;

    private final NewsSourceRepository newsSourceRepository;

    private final NewsMapper newsMapper;

    public NewsService(NewsParserService newsParserService,
                       NewsBodyRepository newsBodyRepository,
                       NewsSourceRepository newsSourceRepository,
                       NewsMapper newsMapper) {
        this.newsParserService = newsParserService;
        this.newsBodyRepository = newsBodyRepository;
        this.newsSourceRepository = newsSourceRepository;
        this.newsMapper = newsMapper;
    }

    /**
     * Добавление в БД информацию о новостях.
     *
     * @param url адрес rss страницы с новостями.
     * @throws MalformedURLException при ошибке работы с URL-адреса.
     * @throws JAXBException         при работе с парсером.
     * @throws ParseException        при форматировании строки в дату.
     */
    public void addNews(String url) throws MalformedURLException, JAXBException, ParseException {
        newsParserService.addNews(url);
    }

    /**
     * Получение списка всех новостей за интервал дат.
     *
     * @param dateF дата начала
     * @param dateT дата окончания
     * @return список новостей за заданный интервал дат.
     */
    public Set<NewsDto> getAllNewsBetween(LocalDate dateF, LocalDate dateT) {
        List<NewsSource> allSource = newsSourceRepository.findAll();

        Set<NewsDto> result = new TreeSet<>(comparing(NewsDto::getPublicDate)).descendingSet();
        for (NewsSource source : allSource) {
            Set<NewsDto> newsByName = getNewsByNameBetween(source.getUrl(), dateF, dateT);
            result.addAll(newsByName);
        }

        return result;
    }

    /**
     * Получение списка новостей из определенного источника за интервал дат.
     *
     * @param url   имя источника
     * @param dateF дата начала
     * @param dateT дата окончания
     * @return список новостей определенного источника за заданный интервал дат.
     */
    @Transactional
    public Set<NewsDto> getNewsByNameBetween(String url, LocalDate dateF, LocalDate dateT) {
        List<NewsSource> news = newsSourceRepository.findBy(url);

        Set<NewsDto> result = new TreeSet<>(comparing(NewsDto::getPublicDate)).descendingSet();
        for (LocalDate date = dateF; date.isBefore(dateT.plusDays(1)); date = date.plusDays(1)) {
            if (date.isAfter(now().plusDays(1))) {
                break;
            }
            List<NewsDto> newsDtos = getNewsBySource(news.get(0), date);
            result.addAll(newsDtos.stream().peek(n -> n.setSource(news.get(0).getName())).toList());
        }

        return result;
    }

    @Transactional
    public List<NewsDto> getNewsBySource(NewsSource newsSource, LocalDate date) {
        OffsetDateTime start = of(date.atStartOfDay(), UTC);
        OffsetDateTime end = of(date.atTime(MAX), UTC);

        List<NewsBody> newsBodies = newsBodyRepository.findAllBy(newsSource.getName(), start, end);
        return newsMapper.convertNewsListToDtoList(newsBodies);
    }
}
