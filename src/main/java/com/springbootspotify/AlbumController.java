package com.springbootspotify;

import com.springbootspotify.entity.Album;
import com.springbootspotify.repo.AlbumRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class AlbumController {

    private final SpotifyAlbumClient spotifyAlbumClient;
    private AlbumRepo albumRepo;


    @Autowired
    public AlbumController(SpotifyAlbumClient spotifyAlbumClient, AlbumRepo albumRepo) {
        this.spotifyAlbumClient = spotifyAlbumClient;
        this.albumRepo = albumRepo;
    }

    @GetMapping("/")
    public String searchForm() {
        return "search";
    }

    @PostMapping("/search")
    public String searchAlbums(Principal principal, @RequestParam("authorName") String authorName, Model model) {
        List<Map<String, Object>> albumList = spotifyAlbumClient.searchAlbums(principal, authorName);
        model.addAttribute("albums", albumList);
        return "search";
    }


    @PostMapping("/add-album")
    public String saveAlbum(@RequestParam("albumName") String albumName, @RequestParam("albumImage") String albumImage, RedirectAttributes redirectAttributes) {
        Album album = new Album();
        album.setAlbumName(albumName);
        album.setAlbumImage(albumImage);
        albumRepo.save(album);
        redirectAttributes.addFlashAttribute("message", "Album added successfully");
        return "redirect:/";
    }

    @GetMapping("/albums")
    public String viewAlbums(Model model) {
        List<Album> albums = albumRepo.findAll();
        model.addAttribute("albums", albums);
        return "albums";
    }

    @PostMapping("/delete-album")
    public String deleteAlbum(@RequestParam("albumId") String albumId, RedirectAttributes redirectAttributes) {
        albumRepo.deleteById(albumId);
        redirectAttributes.addFlashAttribute("message", "Album deleted successfully");
        return "redirect:/albums";
    }


}
