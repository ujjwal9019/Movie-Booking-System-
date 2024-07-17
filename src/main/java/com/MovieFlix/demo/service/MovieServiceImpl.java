package com.MovieFlix.demo.service;

import com.MovieFlix.demo.dto.MovieDto;
import com.MovieFlix.demo.entities.Movie;
import com.MovieFlix.demo.repositories.MovieRespository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))) {
            throw new FileAlreadyExistsException("File already exists! Please enter another file name!");
        }

        String uploadFileName = fileService.uploadFile(path , file);

        // set the value of feild 'poster as filename'
        movieDto.setPoster(uploadFileName);

        //map dto to movie object object
        Movie movie = new Movie(
                null,
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

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        // 1. check if movie object exists with given movieId
        Movie mv = movieRespository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with id = " + movieId));

        // 2. if file is null, do nothing
        // if file is not null, then delete existing file associated with the record,
        // and upload the new file
        String fileName = mv.getPoster();
        if (file != null) {
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = fileService.uploadFile(path, file);
        }

        // 3. set movieDto's poster value, according to step2
        movieDto.setPoster(fileName);

        // 4. map it to Movie object
        Movie movie = new Movie(
                mv.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        // 5. save the movie object -> return saved movie object
        Movie updatedMovie = movieRespository.save(movie);

        // 6. generate posterUrl for it
        String posterUrl = baseUrl + "/file/" + fileName;

        // 7. map to MovieDto and return it
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
    public String deleteMovie(Integer movieId) throws  IOException {

//        1  check if movie object exists in Db
        Movie mv =  movieRespository.findById(movieId).orElseThrow(() -> new RuntimeException());

        Integer id = mv.getMovieId();

// Delete the file associated with this objject

        Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));

        //delete the movie object
        movieRespository.delete(mv);


//        delete the mivie object
return "Movie dleted  with id = " + id ;




    }


}
