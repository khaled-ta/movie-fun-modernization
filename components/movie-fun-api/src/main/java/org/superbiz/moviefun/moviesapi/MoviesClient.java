package org.superbiz.moviefun.moviesapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Component
public class MoviesClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String moviesUrl;
    private final RestOperations restOperations;

    private static ParameterizedTypeReference<List<MovieInfo>> movieListType = new ParameterizedTypeReference<List<MovieInfo>>() {
    };

    public MoviesClient(@Value("${movies.url}") String moviesUrl, RestOperations restOperations) {

        this.moviesUrl = moviesUrl;
        this.restOperations = restOperations;
    }


    public MovieInfo find(Long id) {
        return restOperations.getForObject(moviesUrl + "/{id}", MovieInfo.class, id);
    }

    public void addMovie(MovieInfo movieInfo) {
        logger.debug("Creating movieInfo with title {}, and year {}", movieInfo.getTitle(), movieInfo.getYear());

        restOperations.postForEntity(moviesUrl, movieInfo, MovieInfo.class);
    }

    // todo fix
    public void updateMovie(MovieInfo movieInfo) {
        restOperations.put(moviesUrl, movieInfo);
    }

    // todo fix
    public void deleteMovie(MovieInfo movieInfo) {
        restOperations.delete(moviesUrl, movieInfo);
    }

    public void deleteMovieId(long id) {
        URI uri = UriComponentsBuilder.fromHttpUrl(moviesUrl + "/{id}").buildAndExpand(id).toUri();
        restOperations.delete(uri);
    }

    public List<MovieInfo> getMovies() {
        MovieInfo[] movieInfos = restOperations.getForObject(moviesUrl, MovieInfo[].class);
        return Arrays.asList(movieInfos);
    }

    public List<MovieInfo> findAll(int firstResult, int maxResults) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moviesUrl)
                .queryParam("start", firstResult)
                .queryParam("pageSize", maxResults);

        return restOperations.exchange(builder.toUriString(), HttpMethod.GET, null, movieListType).getBody();
    }

    public int countAll() {

        return restOperations.getForObject(moviesUrl + "/count", Integer.class);
    }

    public int count(String field, String searchTerm) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moviesUrl + "/count")
                .queryParam("field", field)
                .queryParam("key", searchTerm);

        return restOperations.getForObject(builder.toUriString(), Integer.class);
    }

    public List<MovieInfo> findRange(String field, String searchTerm, int firstResult, int maxResults) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moviesUrl)
                .queryParam("field", field)
                .queryParam("key", searchTerm)
                .queryParam("start", firstResult)
                .queryParam("pageSize", maxResults);

        return restOperations.exchange(builder.toUriString(), HttpMethod.GET, null, movieListType).getBody();
    }

    public void clean() {
        restOperations.delete(moviesUrl + "/clean");
    }
}
