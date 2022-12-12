package ru.develonica.model.mapper;

import org.mapstruct.Mapper;
import ru.develonica.model.dto.CurrencyRateDto;
import ru.develonica.model.dto.CurrencyTypeDto;
import ru.develonica.model.entity.CurrencyRate;
import ru.develonica.model.entity.CurrencyType;

/**
 * Маппер объекта Entity в объект DTO.
 */
@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyTypeDto currencyTypeEntityToDto(CurrencyType currencyType);

    CurrencyRateDto currencyRateEntityToDto(CurrencyRate currencyRate);
}
