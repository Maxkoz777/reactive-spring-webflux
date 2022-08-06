package com.reactivespring.moviesinfoservice.domain.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class MovieInfoDto {
    private String name;
    private Integer year;
    private List<String> cast;
    private LocalDate releaseDate;
}
