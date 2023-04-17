package com.springbootspotify.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Album {

    private String id;
    private String AlbumName;
    private String albumImage;

    public String getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumName() {
        return AlbumName;
    }

    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }
}
