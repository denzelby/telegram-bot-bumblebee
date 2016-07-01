package com.github.bumblebee.command.youtube.service;

import com.github.bumblebee.command.youtube.dao.ChatIdForYoutubeRepository;
import com.github.bumblebee.command.youtube.entity.ChatId;
import com.google.api.client.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ChatIdForYoutubeService {

    private ChatIdForYoutubeRepository repository;

    @Autowired
    public ChatIdForYoutubeService(ChatIdForYoutubeRepository repository) {
        this.repository = repository;
    }

    public void storeChatId(ChatId chatId) {
        repository.save(chatId);
    }

    public List<ChatId> retrieveChatIds() {
        return Lists.newArrayList(repository.findAll());
    }
}
