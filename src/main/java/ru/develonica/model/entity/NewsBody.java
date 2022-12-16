package ru.develonica.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
import java.time.OffsetDateTime;

/**
 * Новости.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "newsSource")
@EqualsAndHashCode(of = "urlNews")
@Builder
@Entity
@Table(name = "news_body")
public class NewsBody implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "source_id", insertable = false, updatable = false)
    private Integer sourceId;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private NewsSource newsSource;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column
    private String description;

    @Column(unique = true, nullable = false)
    private String urlNews;

    @Column(nullable = false)
    private OffsetDateTime publicDate;
}
