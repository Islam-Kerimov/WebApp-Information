package ru.develonica.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Объекта курса валют для отображения клиенту.
 */
@Data
@JsonPropertyOrder({"valute", "value", "date"})
public class CurrencyRateDto implements Serializable {
    @JsonProperty("valute")
    private CurrencyTypeDto currencyTypeDto;
    private Double value;
    private LocalDate date;
}
