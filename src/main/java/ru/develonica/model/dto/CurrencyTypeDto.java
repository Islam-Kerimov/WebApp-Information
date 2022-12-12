package ru.develonica.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Объекта типа валюты для отображения клиенту.
 */
@Getter
@Setter
public class CurrencyTypeDto {
    private String numCode;
    private String charCode;
    private Integer logNominal;
    private String name;
}
