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
 * Валюта.
 */
@Data
@NoArgsConstructor
@ToString(exclude = "values")
@EqualsAndHashCode(of = "numCode")
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "currencyType")
    private List<CurrencyRate> values = new ArrayList<>(0);
}
