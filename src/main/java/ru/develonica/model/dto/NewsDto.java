package ru.develonica.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * Объекта новостей для отображения клиенту.
 */
@Data
@JsonPropertyOrder({"source", "title", "category", "description", "url", "publicDate"})
public class NewsDto {
    private String source;
    private String title;
    private String category;
    private String description;
    @JsonProperty("url")
    private String urlNews;
    private OffsetDateTime publicDate;
}
