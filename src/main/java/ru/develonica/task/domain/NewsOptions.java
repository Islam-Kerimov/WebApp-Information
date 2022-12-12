package ru.develonica.task.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Объект с опциями новостей.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsOptions implements Options {
    private String url;
}
