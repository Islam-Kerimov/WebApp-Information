package ru.develonica.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Валюта.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "currency_type")
public class CurrencyType implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private Integer id;

    @Column(columnDefinition = "BPCHAR(3)", unique = true, nullable = false)
    private String numCode;

    @Column(columnDefinition = "BPCHAR(3)", unique = true, nullable = false)
    private String charCode;

    @Column(columnDefinition = "INT2", nullable = false)
    private Integer logNominal;

    @Column(unique = true, nullable = false)
    private String name;
}
