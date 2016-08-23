package com.github.bumblebee.command.youtube.service;

import com.github.bumblebee.command.youtube.entity.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class SubscriptionUpdateSheduler {

    private final YoutubeSubscriptionService service;
    private static final long delay = 60 * 1000 * 60 * 4; //4 Hours in millis
    private static final long overdueInterval = TimeUnit.DAYS.toMillis(4);

    @Autowired
    public SubscriptionUpdateSheduler(YoutubeSubscriptionService service) {
        this.service = service;
    }

    @Scheduled(fixedRate = delay)
    public void checkOverdueSubscriptions() {
        Date date = new Date();
        for (Subscription sub : service.getExistingSubscriptions()) {
            long interval = date.getTime() - sub.getUpdatedDate().getTime();
            if (interval > overdueInterval) {
                sub.setUpdatedDate(date);
                if (service.subscribeChannel(sub.getChannelId()))
                    service.storeSubscription(sub);
            }
        }
    }

}
