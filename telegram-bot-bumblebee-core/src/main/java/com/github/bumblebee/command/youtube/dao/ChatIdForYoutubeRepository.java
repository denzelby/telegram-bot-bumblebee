package com.github.bumblebee.command.youtube.dao;

import com.github.bumblebee.command.youtube.entity.ChatId;
import org.springframework.data.repository.CrudRepository;

public interface ChatIdForYoutubeRepository extends CrudRepository<ChatId, Long> {
}
