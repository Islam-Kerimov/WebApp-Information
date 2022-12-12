package ru.develonica.task.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Объект с опциями погоды.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherOptions implements Options {
    private String cityId;
}
