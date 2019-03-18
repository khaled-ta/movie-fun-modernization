package org.superbiz.moviefun.cfsupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private MoviesRepository moviesRepository;

    public MoviesController(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    @GetMapping(value = "/{id}")
    public Movie find(@PathVariable Long id) {
        return moviesRepository.find(id);
    }

    @PostMapping
    public void addMovie(@RequestBody Movie movie) {
        logger.debug("Creating movie with title {}, and year {}", movie.getTitle(), movie.getYear());

        moviesRepository.addMovie(movie);
    }

    @PutMapping
    public void updateMovie(Movie movie) {
        moviesRepository.updateMovie(movie);
    }

    @DeleteMapping
    public void deleteMovie(Movie movie) {
        moviesRepository.deleteMovie(movie);
    }

    @DeleteMapping("/{id}")
    public void deleteMovieId(@PathVariable long id) {
        moviesRepository.deleteMovieId(id);
    }

    @GetMapping("/count")
    public int count(@RequestParam(name = "field", required = false) String field,
                     @RequestParam(name = "key", required = false) String searchTerm) {

        if (field != null && searchTerm != null) {
            return moviesRepository.count(field, searchTerm);
        } else {
            return moviesRepository.countAll();
        }
    }

    @GetMapping
    public List<Movie> findRange(@RequestParam(name = "field", required = false) String field,
                                 @RequestParam(name = "key", required = false) String searchTerm,
                                 @RequestParam(name = "start", required = false) Integer firstResult,
                                 @RequestParam(name = "pageSize", required = false) Integer maxResults) {
        if (field != null && searchTerm != null) {
            return moviesRepository.findRange(field, searchTerm, firstResult, maxResults);
        } else if (firstResult != null && maxResults != null) {
            return moviesRepository.findAll(firstResult, maxResults);
        } else {
            return moviesRepository.getMovies();
        }
    }

    public void clean() {
        moviesRepository.clean();
    }

}
