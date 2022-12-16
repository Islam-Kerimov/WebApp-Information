package ru.develonica.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.develonica.model.dto.NewsDto;
import ru.develonica.model.entity.NewsBody;
import ru.develonica.model.mapper.NewsMapper;
import ru.develonica.service.NewsService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/news")
public class NewsController {

    private final NewsService newsService;

    private final NewsMapper newsMapper;

    public NewsController(NewsService newsService, NewsMapper newsMapper) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
    }

    /** Получение списка всех новостей за интервал дат. */
    @GetMapping("/period")
    public List<NewsDto> getAllWeatherBetween(@RequestParam LocalDate dateF,
                                              @RequestParam LocalDate dateT) {
        List<NewsBody> newsBodies = newsService.getAllNewsBetween(dateF, dateT);
        return newsMapper.entityNewsListToDtoList(newsBodies);
    }

    /** Получение списка всех новостей заданного источника за интервал дат. */
    @GetMapping("/period/{name}")
    public List<NewsDto> getAllNewsByNameBetween(@PathVariable String name,
                                                 @RequestParam LocalDate dateF,
                                                 @RequestParam LocalDate dateT) {
        List<NewsBody> newsBodies = newsService.getNewsByNameBetween(name, dateF, dateT);
        return newsMapper.entityNewsListToDtoList(newsBodies);
    }
}
