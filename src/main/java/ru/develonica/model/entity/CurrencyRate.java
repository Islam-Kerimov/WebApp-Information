package ru.develonica.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Курс валюты.
 */
@Data
@NoArgsConstructor
@ToString(exclude = "currencyType")
@EqualsAndHashCode(of = {"id", "value", "date"})
@Entity
@Table(name = "currency_rate")
public class CurrencyRate implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "currency_id", insertable = false, updatable = false)
    private Integer currencyId;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private CurrencyType currencyType;

    @Column(columnDefinition = "NUMERIC")
    private Double value;

    @Column(nullable = false)
    private LocalDate date;
}
