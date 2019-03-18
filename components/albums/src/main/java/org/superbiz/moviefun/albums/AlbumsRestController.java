package org.superbiz.moviefun.albums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumsRestController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AlbumsRepository albumsRepository;

    public AlbumsRestController(AlbumsRepository albumsRepository) {
        this.albumsRepository = albumsRepository;
    }

    @GetMapping
    public List<Album> getAll() {
        return albumsRepository.getAlbums();
    }

    @GetMapping(value = "/{id}")
    public Album find(@PathVariable Long id) {
        return albumsRepository.find(id);
    }

    @PostMapping
    public void addAlbum(@RequestBody Album album) {
        logger.debug("Creating album with title {}, and year {}", album.getTitle(), album.getYear());

        albumsRepository.addAlbum(album);
    }

    @PutMapping
    public void updateMovie(Album album) {
        albumsRepository.updateAlbum(album);
    }

    @DeleteMapping("/{id}")
    public void deleteAlbum(@PathVariable Long id) {
        Album album = albumsRepository.find(id);
        if (album == null) {
            throw new IllegalArgumentException("album not found by supplied id: " + id);
        }
        albumsRepository.deleteAlbum(album);
    }
}
