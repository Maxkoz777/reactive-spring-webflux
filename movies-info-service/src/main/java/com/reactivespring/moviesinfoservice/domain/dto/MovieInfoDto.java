package com.reactivespring.moviesinfoservice.domain.dto;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfoDto {
    @NotBlank
    private String name;
    @NotNull
    @Positive
    private Integer year;
    @Size(min = 1)
    private List<String> cast;
    @PastOrPresent
    private LocalDate releaseDate;
}
