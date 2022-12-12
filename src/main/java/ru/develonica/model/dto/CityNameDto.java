package ru.develonica.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Объекта отображения погоды города клиенту.
 */
@Getter
@Setter
public class CityNameDto {
    private Integer id;
    private String name;
    private Double longitude;
    private Double latitude;
}
