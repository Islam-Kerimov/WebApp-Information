package ru.develonica.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Источник новостей.
 */
@Data
@NoArgsConstructor
@ToString(exclude = "values")
@EqualsAndHashCode(of = "url")
@Entity
@Table(name = "news_source")
public class NewsSource implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_id")
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String url;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "newsSource")
    private List<NewsBody> values = new ArrayList<>(0);

    public NewsSource(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
