package ru.develonica.model.mapper;

import org.mapstruct.Mapper;
import ru.develonica.model.dto.CityNameDto;
import ru.develonica.model.dto.WeatherDto;
import ru.develonica.model.entity.CityName;
import ru.develonica.model.entity.Weather;

/**
 * Маппер объекта Entity в объект DTO.
 */
@Mapper(componentModel = "spring")
public interface WeatherMapper {

    WeatherDto weatherEntityToDto(Weather weather);

    CityNameDto cityNameEntityToDto(CityName cityName);
}
