package ru.develonica.model.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.develonica.model.dto.CityNameDto;
import ru.develonica.model.dto.WeatherDto;
import ru.develonica.model.entity.Weather;

import java.util.List;

/**
 * Маппер объекта Entity в объект DTO.
 */
@Mapper(componentModel = "spring", uses = CityNameDto.class)
public interface WeatherMapper {

    @Named("FullForm")
    @Mapping(target = "cityNameDto", source = "cityName")
    WeatherDto entityWeatherToDto(Weather weather);

    @IterableMapping(qualifiedByName = "FullForm")
    List<WeatherDto> entityWeatherListToDtoList(List<Weather> weather);
}
