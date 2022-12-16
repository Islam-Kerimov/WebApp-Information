package ru.develonica.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static java.lang.Double.valueOf;
import static java.time.Instant.ofEpochSecond;
import static java.time.LocalDateTime.ofInstant;
import static java.time.OffsetDateTime.of;
import static java.time.ZoneId.systemDefault;
import static java.time.ZoneOffset.ofTotalSeconds;

/**
 * Погода.
 */
@Data
@NoArgsConstructor
@ToString(exclude = "cityName")
@EqualsAndHashCode(of = {"cityId", "name", "description", "dateCreate"})
@Entity
@Table(name = "weather")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "city_id", insertable = false, updatable = false)
    private Integer cityId;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private CityName cityName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(columnDefinition = "NUMERIC", nullable = false)
    private Double temperature;

    @Column(columnDefinition = "NUMERIC", nullable = false)
    private Double temperatureMin;

    @Column(columnDefinition = "NUMERIC", nullable = false)
    private Double temperatureMax;

    @Column(columnDefinition = "INT2", nullable = false)
    private Integer humidity;

    @Column(columnDefinition = "INT2", nullable = false)
    private Integer pressure;

    @Column(nullable = false)
    private Integer visibility;

    @Column(columnDefinition = "NUMERIC", nullable = false)
    private Double windSpeed;

    @Column(columnDefinition = "INT2", nullable = false)
    private Integer cloudiness;

    @Column(nullable = false)
    private OffsetDateTime dateCreate;

    @Transient
    @JsonProperty("dt")
    private Long dt;

    @Column(nullable = false)
    private Integer timezone;

    public void setDateCreate(Long dateTime) {
        this.dateCreate = of(
                ofInstant(ofEpochSecond(dateTime), systemDefault()),
                ofTotalSeconds(timezone));
    }

    @JsonProperty("weather")
    private void unpackWeather(List<Map<String, String>> weathers) {
        Map<String, String> weather = weathers.get(0);
        this.name = weather.get("main");
        this.description = weather.get("description");
    }

    @JsonProperty("main")
    private void unpackMain(Map<String, String> main) {
        this.temperature = valueOf(main.get("temp"));
        this.temperatureMin = valueOf(main.get("temp_min"));
        this.temperatureMax = valueOf(main.get("temp_max"));
        this.humidity = Integer.valueOf(main.get("humidity"));
        this.pressure = Integer.valueOf(main.get("pressure"));
    }

    @JsonProperty("wind")
    private void unpackWind(Map<String, String> wind) {
        this.windSpeed = valueOf(wind.get("speed"));
    }

    @JsonProperty("clouds")
    private void unpackCloud(Map<String, String> cloud) {
        this.cloudiness = Integer.valueOf(cloud.get("all"));
    }


}
