package telegram.polling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import telegram.api.BotApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TelegramUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(TelegramUpdateService.class);

    private ScheduledExecutorService executor;
    private ScheduledFuture<?> scheduledFuture;
    private final Runnable customUpdateAction;
    private final List<UpdateHandler> handlers = new ArrayList<>();

    private final BotApi botApi;
    private int pollInterval = 30;
    private TimeUnit pollIntervalUnit = TimeUnit.SECONDS;

    public TelegramUpdateService(BotApi botApi, int pollInterval, TimeUnit timeUnit) {
        this(botApi, pollInterval, timeUnit, null);
    }

    public TelegramUpdateService(BotApi botApi, int pollInterval, TimeUnit timeUnit, Runnable customUpdateAction) {
        this.botApi = botApi;
        this.pollInterval = pollInterval;
        this.pollIntervalUnit = timeUnit;
        this.customUpdateAction = customUpdateAction;
    }

    public synchronized void startPolling() {

        if (executor == null) {
            executor = Executors.newSingleThreadScheduledExecutor();
        }

        final Runnable action = (customUpdateAction != null)
                ? customUpdateAction
                : new TelegramUpdateAction(botApi, handlers);
        scheduledFuture = executor.scheduleWithFixedDelay(action, 0, pollInterval, pollIntervalUnit);

        LOG.debug("Polling started");
    }

    public synchronized void stopPolling() {

        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        if (executor != null) {
            executor.shutdown();
        }

        LOG.debug("Polling stopped");
    }

    public void onUpdate(UpdateHandler handler) {
        handlers.add(handler);
    }
}
