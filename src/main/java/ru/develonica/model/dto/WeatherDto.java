package ru.develonica.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * Объекта погоды для отображения клиенту.
 */
@Getter
@Setter
@JsonPropertyOrder({"city"})
public class WeatherDto {
    @JsonProperty("city")
    private CityNameDto cityNameDto;
    private String name;
    private String description;
    private Double temperature;
    private Double temperatureMin;
    private Double temperatureMax;
    private Integer humidity;
    private Integer pressure;
    private Integer visibility;
    private Double windSpeed;
    private Integer cloudiness;
    private OffsetDateTime dateCreate;
    private Integer timezone;
}
