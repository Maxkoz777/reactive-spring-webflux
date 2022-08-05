package com.reactivespring.moviesinfoservice.domain.mapper;

import com.reactivespring.moviesinfoservice.domain.dto.MovieInfoDto;
import com.reactivespring.moviesinfoservice.domain.entity.MovieInfo;
import org.mapstruct.Mapper;

@Mapper
public interface MovieInfoMapper {

    MovieInfo movieInfoDtoToMovieInfo(MovieInfoDto movieInfoDto);

    MovieInfoDto movieInfoToMovieInfoDto(MovieInfo movieInfo);

}
