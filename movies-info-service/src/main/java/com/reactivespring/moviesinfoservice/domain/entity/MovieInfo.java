package com.reactivespring.moviesinfoservice.domain.entity;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfo {

    @Id
    private String movieInfoId;
    private String name;
    private Integer year;
    private List<String> cast;
    private LocalDate releaseDate;


}
