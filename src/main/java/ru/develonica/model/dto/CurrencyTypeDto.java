package ru.develonica.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Объекта типа валюты для отображения клиенту.
 */
@Data
public class CurrencyTypeDto implements Serializable {
    private String numCode;
    private String charCode;
    private Integer logNominal;
    private String name;
}
