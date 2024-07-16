package com.MovieFlix.demo.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

//This class is responsible for intrecting with controller and mapping the data
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MovieDto {
   private Integer movieId;


    @NotBlank(message = "Please Provide Movies Title")
    private String title;


    @NotBlank(message = "Please Provide Movies director")
    private String director;


    @NotBlank(message = "Please Provide Movies studio")
    private String studio;


    private Set<String> movieCast;

 @NotNull(message = "Please provide the movie's release year")
    private Integer releaseYear;


    @NotBlank(message = "Please Provide Movies poster")
    private String poster;

    @NotBlank(message = "please provide poster 's url")
    private String posterUrl;
}
