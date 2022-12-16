package ru.develonica.model.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.develonica.model.dto.NewsDto;
import ru.develonica.model.entity.NewsBody;

import java.util.List;

/**
 * Маппер объекта Entity в объект DTO.
 */
@Mapper(componentModel = "spring")
public interface NewsMapper {

    @Named("FullForm")
    @Mapping(target = "source", source = "newsSource.name")
    NewsDto entityNewsToDto(NewsBody newsBody);

    @IterableMapping(qualifiedByName = "FullForm")
    List<NewsDto> entityNewsListToDtoList(List<NewsBody> newsBodies);
}
