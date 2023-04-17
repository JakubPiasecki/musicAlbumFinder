package com.springbootspotify.repo;

import com.springbootspotify.entity.Album;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepo extends MongoRepository<Album, String> {
}
