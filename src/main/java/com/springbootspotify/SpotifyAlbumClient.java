package com.springbootspotify;

import com.springbootspotify.models.AlbumsResponse;
import com.springbootspotify.models.Artist;
import com.springbootspotify.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SpotifyAlbumClient {

    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public SpotifyAlbumClient(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/album/{authorName}")
    @ResponseBody
    public List<Map<String, Object>> searchAlbums(Principal principal, @PathVariable String authorName) {
        if (principal == null) {
            throw new IllegalStateException("Cannot access authorized client without principal");
        }

        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("spotify", principal.getName());

        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        return searchAlbums(authorName, accessToken);
    }

    private List<Map<String, Object>> searchAlbums(String authorName, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://api.spotify.com/v1/search?q=" + authorName + "&type=album&limit=20";

        ResponseEntity<AlbumsResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, AlbumsResponse.class);

        AlbumsResponse albumsResponse = response.getBody();

        List<Map<String, Object>> albumDetailsList = new ArrayList<>();

        if (albumsResponse != null && albumsResponse.getAlbums() != null && !albumsResponse.getAlbums().getItems().isEmpty()) {
            List<Item> albums = albumsResponse.getAlbums().getItems();
            for (Item album : albums) {
                List<Artist> artists = album.getArtists();
                boolean isAuthorPresent = artists.stream().map(Artist::getName).toList().contains(authorName);

                if (isAuthorPresent) {
                    String albumName = album.getName();
                    String albumImage = album.getImages().get(0).getUrl();

                    Map<String, Object> albumDetails = new HashMap<>();
                    albumDetails.put("albumName", albumName);
                    albumDetails.put("albumImage", albumImage);

                    albumDetailsList.add(albumDetails);
                }
            }
        }

        return albumDetailsList;
    }


}
