package ru.develonica.service;

import org.springframework.stereotype.Service;
import ru.develonica.model.entity.NewsBody;
import ru.develonica.repository.NewsBodyRepository;

import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;
import static java.time.LocalTime.MAX;
import static java.time.OffsetDateTime.of;
import static java.time.ZoneOffset.UTC;

/**
 * Сервис отвечающий за работу с информацией о новостях.
 */
@Service
public class NewsService {

    private static final String LIKE_FORMAT = "%%%s%%";

    private final NewsBodyRepository newsBodyRepository;

    public NewsService(NewsBodyRepository newsBodyRepository) {
        this.newsBodyRepository = newsBodyRepository;
    }

    /**
     * Получение списка всех новостей за интервал дат.
     *
     * @param dateF дата начала
     * @param dateT дата окончания
     * @return список новостей за заданный интервал дат.
     */
    public List<NewsBody> getAllNewsBetween(LocalDate dateF, LocalDate dateT) {
        return newsBodyRepository.findAllByPublicDateBetweenOrderByPublicDateDesc(of(dateF.atStartOfDay(), UTC), of(dateT.atTime(MAX), UTC));
    }

    /**
     * Получение списка новостей из определенного источника за интервал дат.
     *
     * @param url   имя источника
     * @param dateF дата начала
     * @param dateT дата окончания
     * @return список новостей определенного источника за заданный интервал дат.
     */
    public List<NewsBody> getNewsByNameBetween(String url, LocalDate dateF, LocalDate dateT) {
        return newsBodyRepository.findAllBySourceBetween(format(LIKE_FORMAT, url), dateF, dateT);
    }
}
