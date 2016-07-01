package com.github.bumblebee.command.youtube.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "BB_CHATID_YOUTUBE")
public class ChatId {

    @Id
    private Long chatId;

    public ChatId() {
    }

    public ChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "ChatId: " + chatId;
    }
}
