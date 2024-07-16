package com.MovieFlix.demo.service;

import com.MovieFlix.demo.dto.MovieDto;
import com.MovieFlix.demo.entities.Movie;
import com.MovieFlix.demo.repositories.MovieRespository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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

        // Step 1 = to check in db that it exists or not
       Movie movie =  movieRespository.findById(movieId).orElseThrow(() -> new RuntimeException());

//        Generate poster url
        String posterUrl = baseUrl + "/file/" + movie.getPoster();

        //map Movie object to Dto object and retiurn it
        MovieDto response = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );





        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
   // fetch all data from db
      List<Movie> movies  = movieRespository.findAll();

      List<MovieDto> movieDtos = new ArrayList<>();

      // iterate through the list , generate posterUrl for each movie obj ,
      //a and map to movieDto

      for(Movie movie : movies){
          String posterUrl = baseUrl + "/file/" + movie.getPoster();

          MovieDto movieDto = new MovieDto(
                  movie.getMovieId(),
                  movie.getTitle(),
                  movie.getDirector(),
                  movie.getStudio(),
                  movie.getMovieCast(),
                  movie.getReleaseYear(),
                  movie.getPoster(),
                  posterUrl
          );
          movieDtos.add(movieDto);

      }
      return movieDtos;



    }
}
