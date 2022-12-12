package ru.develonica.model.mapper;

import org.mapstruct.Mapper;
import ru.develonica.model.dto.NewsDto;
import ru.develonica.model.entity.NewsBody;

import java.util.List;

/**
 * Маппер объекта Entity в объект DTO.
 */
@Mapper(componentModel = "spring")
public interface NewsMapper {

    NewsDto newsEntityToDto(NewsBody newsBody);

    List<NewsDto> convertNewsListToDtoList(List<NewsBody> bodies);
}
