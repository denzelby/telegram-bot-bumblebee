package com.github.bumblebee.command.youtube.dao

import com.github.bumblebee.command.youtube.entity.PostedVideo
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface YoutubePostedVideosRepository : CrudRepository<PostedVideo, String>
