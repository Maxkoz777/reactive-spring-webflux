package com.reactivespring.moviesinfoservice.domain.mapper;

import com.reactivespring.moviesinfoservice.domain.dto.MovieInfoDto;
import com.reactivespring.moviesinfoservice.domain.entity.MovieInfo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface MovieInfoMapper {

    MovieInfo movieInfoDtoToMovieInfo(MovieInfoDto movieInfoDto);

    MovieInfoDto movieInfoToMovieInfoDto(MovieInfo movieInfo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMovieInfoFromDto(MovieInfoDto dto, @MappingTarget MovieInfo movieInfo);

}
