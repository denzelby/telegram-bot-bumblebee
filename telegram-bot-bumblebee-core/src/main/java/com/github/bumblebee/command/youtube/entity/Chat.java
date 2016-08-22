package com.github.bumblebee.command.youtube.entity;

import javax.persistence.*;


@Entity
@Table(name = "BB_YOUTUBE_SUBBED_CHATS")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "SUBSCRIPTION_SUBID")
    private Subscription subscription;

    public Chat() {
    }

    public Chat(Long chatId, Subscription subscription) {
        this.chatId = chatId;
        this.subscription = subscription;
    }


    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chat(Long chatId) {
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
        return "chatId: " + chatId;
    }
}
