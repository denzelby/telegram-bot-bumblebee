package com.github.bumblebee.command.youtube.dao;

import com.github.bumblebee.command.youtube.entity.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface YoutubeSubscriptionRepository extends CrudRepository<Subscription, Long> {

}
