package com.MovieFlix.demo.service;

import com.MovieFlix.demo.dto.MovieDto;
import com.MovieFlix.demo.entities.Movie;
import com.MovieFlix.demo.repositories.MovieRespository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MovieServiceImpl implements  MovieService{


//    Hrr baar ye do cheeze add krni padegi since we have to deal with db
    private final MovieRespository movieRespository;

    private final FileService fileService;

    @Value("$(project.poster)")
    private String path;

    @Value("$(base.url)")
    private String baseUrl;

    public MovieServiceImpl(MovieRespository movieRespository , FileService fileService){
        this.movieRespository = movieRespository;
        this.fileService = fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
       //upload the file
        String uploadFileName = fileService.uploadFile(path , file);

        // set the value of feild 'poster as filename'
        movieDto.setPoster(uploadFileName);

        //map dto to movie object object
        Movie movie = new Movie(
                movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

//        save the movie object
    Movie savedMovie = movieRespository.save(movie);

//    generate the posterUrl

String posterUrl = baseUrl + "/file/" + uploadFileName;


//map Movie object to Dto object and retiurn it
        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );

        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        return null;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        return List.of();
    }
}
