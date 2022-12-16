package ru.develonica.model.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.develonica.model.dto.CurrencyRateDto;
import ru.develonica.model.dto.CurrencyTypeDto;
import ru.develonica.model.entity.CurrencyRate;

import java.util.List;

/**
 * Маппер объекта Entity в объект DTO.
 */
@Mapper(componentModel = "spring", uses = CurrencyTypeDto.class)
public interface CurrencyRateMapper {

    @Named("FullForm")
    @Mapping(target = "currencyTypeDto", source = "currencyType")
    CurrencyRateDto entityRateToDto(CurrencyRate currencyRate);

    @IterableMapping(qualifiedByName = "FullForm")
    List<CurrencyRateDto> entityRateListToDtoList(List<CurrencyRate> currencyRates);
}
