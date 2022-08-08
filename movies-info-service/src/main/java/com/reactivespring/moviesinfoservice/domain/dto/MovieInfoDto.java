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
    @NotBlank(message = "name should be specified")
    private String name;
    @NotNull(message = "year should be specified")
    @Positive(message = "year should be positive")
    private Integer year;
    @Size(min = 1, message = "specify at least 1 actor/actress")
    private List<String> cast;
    @PastOrPresent(message = "Date should be in the past")
    private LocalDate releaseDate;
}
