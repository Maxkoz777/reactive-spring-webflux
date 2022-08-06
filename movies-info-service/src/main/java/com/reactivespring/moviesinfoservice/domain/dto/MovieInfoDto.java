package com.reactivespring.moviesinfoservice.domain.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfoDto {
    private String name;
    private Integer year;
    private List<String> cast;
    private LocalDate releaseDate;
}
