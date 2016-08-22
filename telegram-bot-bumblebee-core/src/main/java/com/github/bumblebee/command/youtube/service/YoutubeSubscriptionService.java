package com.github.bumblebee.command.youtube.service;

import com.github.bumblebee.command.youtube.dao.YoutubeSubscriptionRepository;
import com.github.bumblebee.command.youtube.entity.Subscription;
import com.google.api.client.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class YoutubeSubscriptionService {

    private YoutubeSubscriptionRepository repository;

    @Autowired
    public YoutubeSubscriptionService(YoutubeSubscriptionRepository repository) {
        this.repository = repository;
    }

    public void storeSubscription(Subscription subscription) {
        repository.save(subscription);
    }

    public List<Subscription> retrieveSubscriptions() {
        return Lists.newArrayList(repository.findAll());
    }

    public void deleteSubscription(Subscription subscription) {
        repository.delete(subscription);
    }

}
