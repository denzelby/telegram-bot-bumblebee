package com.github.bumblebee.command.youtube.service;

import com.github.bumblebee.command.youtube.YoutubeSubscribeCommand;
import com.github.bumblebee.command.youtube.api.YoutubeSubscriptionProvider;
import com.github.bumblebee.command.youtube.entity.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class SubscriptionUpdateSheduler {

    private final YoutubeSubscribeCommand command;
    private final YoutubeSubscriptionProvider provider;
    private final YoutubeSubscriptionService service;
    private static final long delay = 60 * 1000 * 60 * 4;
    private static final long overdueInterval = TimeUnit.DAYS.toMillis(4);

    @Autowired
    public SubscriptionUpdateSheduler(YoutubeSubscribeCommand command,
                                      YoutubeSubscriptionProvider provider,
                                      YoutubeSubscriptionService service) {
        this.command = command;
        this.provider = provider;
        this.service = service;
    }

    @Scheduled(fixedRate = delay)
    public void checkOverdueSubscriptions() {
        Date date = new Date();
        for (Subscription sub : command.getSubscriptionList()) {
            long interval = date.getTime() - sub.getUpdatedDate().getTime();
            if (interval > overdueInterval) {
                sub.setUpdatedDate(date);
                if (provider.subscribeChannel(sub.getChannelId()))
                    service.storeSubscription(sub);
            }
        }
    }

}
