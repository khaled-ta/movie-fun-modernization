package org.superbiz.moviefun.moviesapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.List;

@Component
public class AlbumsClient {
    private final String albumsUrl;
    private final RestOperations restOperations;

    private static ParameterizedTypeReference<List<AlbumInfo>> albumListType = new ParameterizedTypeReference<List<AlbumInfo>>() {
    };

    public AlbumsClient(@Value("${albums.url}") String albumsUrl, RestOperations restOperations) {

        this.albumsUrl = albumsUrl;
        this.restOperations = restOperations;
    }

    public void addAlbum(AlbumInfo albumInfo) {
        restOperations.postForObject(albumsUrl, albumInfo, AlbumInfo.class);
    }

    public AlbumInfo find(long id) {
        return restOperations.getForObject(albumsUrl + "/{id}", AlbumInfo.class, id);
    }

    public List<AlbumInfo> getAlbums() {
        return restOperations.exchange(albumsUrl, HttpMethod.GET, null, albumListType).getBody();
    }

    public void deleteAlbum(AlbumInfo albumInfo) {
        restOperations.delete(albumsUrl + "/{id}", albumInfo.getId());
    }

    public void updateAlbum(AlbumInfo albumInfo) {
        restOperations.put(albumsUrl, albumInfo, AlbumInfo.class);
    }

}
