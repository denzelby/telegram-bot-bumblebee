package com.github.bumblebee.command.youtube.dao;

import com.github.bumblebee.command.youtube.entity.PostedVideo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Fare on 25.11.2016.
 */
@Repository
public interface YoutubePostedVideosRepository extends CrudRepository<PostedVideo, String> {
}
