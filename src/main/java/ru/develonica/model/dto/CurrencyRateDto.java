package ru.develonica.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Объекта курса валют для отображения клиенту.
 */
@Getter
@Setter
@JsonPropertyOrder({"valute", "value", "date"})
public class CurrencyRateDto {
    @JsonProperty("valute")
    private CurrencyTypeDto currencyTypeDto;
    private Double value;
    private LocalDate date;
}
