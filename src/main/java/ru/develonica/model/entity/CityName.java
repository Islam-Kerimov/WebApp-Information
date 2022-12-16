package ru.develonica.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Double.valueOf;

/**
 * Город отображения погоды.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "values")
@EqualsAndHashCode(of = "name")
@Builder
@Entity
@Table(name = "city_name")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityName implements BaseEntity<Integer> {

    @Id
    @Column(name = "city_id")
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "NUMERIC", nullable = false)
    private Double longitude;

    @Column(columnDefinition = "NUMERIC", nullable = false)
    private Double latitude;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cityName")
    private List<Weather> values = new ArrayList<>(0);

    @JsonProperty("coord")
    private void unpackCoord(Map<String, String> coord) {
        this.longitude = valueOf(coord.get("lon"));
        this.latitude = valueOf(coord.get("lat"));
    }
}
