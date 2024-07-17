package com.MovieFlix.demo.controller;

import com.MovieFlix.demo.dto.MovieDto;
import com.MovieFlix.demo.service.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file,
                                                    @RequestPart String movieDto) throws JsonProcessingException, IOException {

        MovieDto  dto = convertToMovieDto(movieDto);
        return  new ResponseEntity<>(movieService.addMovie(dto , file) , HttpStatus.CREATED);


    }
    @GetMapping("/{movieId}")
    public ResponseEntity<String> getMovieHanler(@PathVariable Integer movieId){
        return new ResponseEntity(movieService.getMovie(movieId) , HttpStatus.OK);

    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllMovisHandler(){
        return  new ResponseEntity(movieService.getAllMovies() , HttpStatus.OK);
    }


    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer movieId,
                                                       @RequestPart MultipartFile file,
                                                       @RequestPart String movieDtoObj) throws IOException {
        if (file.isEmpty()) file = null;
        MovieDto movieDto = convertToMovieDto(movieDtoObj);
        return ResponseEntity.ok(movieService.updateMovie(movieId, movieDto, file));
    }


    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId ) throws IOException {
        return ResponseEntity.ok(movieService.deleteMovie(movieId));
    }







    //to convert string into json
    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(movieDtoObj , MovieDto.class);
    }



}
