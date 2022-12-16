package ru.develonica.model.dto;

import lombok.Data;

/**
 * Объекта отображения погоды города клиенту.
 */
@Data
public class CityNameDto {
    private Integer id;
    private String name;
    private Double longitude;
    private Double latitude;
}
