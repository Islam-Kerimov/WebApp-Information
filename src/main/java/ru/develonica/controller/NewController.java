package ru.develonica.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.develonica.model.dto.NewsDto;
import ru.develonica.service.NewsService;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping(value = "/news")
public class NewController {

    private final NewsService newsService;

    public NewController(NewsService newsService) {
        this.newsService = newsService;
    }

    /** Получение списка всех новостей за интервал дат. */
    @GetMapping("/period")
    public Set<NewsDto> getAllWeatherBetween(@RequestParam LocalDate dateF,
                                             @RequestParam LocalDate dateT) {
        return newsService.getAllNewsBetween(dateF, dateT);
    }

    /** Получение списка всех новостей заданного источника за интервал дат. */
    @GetMapping("/period/{name}")
    public Set<NewsDto> getAllNewsByNameBetween(@PathVariable String name,
                                                @RequestParam LocalDate dateF,
                                                @RequestParam LocalDate dateT) {
        return newsService.getNewsByNameBetween(name, dateF, dateT);
    }
}
