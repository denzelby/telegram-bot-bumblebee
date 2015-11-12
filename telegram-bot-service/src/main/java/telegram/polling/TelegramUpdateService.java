package telegram.polling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import telegram.api.BotApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TelegramUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(TelegramUpdateService.class);

    private ScheduledExecutorService executor;
    private Future<?> future;
    private final Runnable customUpdateAction;
    private final List<UpdateHandler> handlers = new ArrayList<>();
    private final Map<String, UpdateHandler> commandHandlers = new HashMap<>();

    private final BotApi botApi;
    private int pollInterval;
    private TimeUnit pollIntervalUnit = TimeUnit.SECONDS;

    public TelegramUpdateService(BotApi botApi) {
        this(botApi, 1, TimeUnit.SECONDS, null);
    }

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
                : new TelegramUpdateAction(botApi, commandHandlers, handlers);
        future = executor.scheduleWithFixedDelay(action, 0, pollInterval, pollIntervalUnit);

        LOG.debug("Polling started");
    }

    public synchronized void stopPolling() {

        if (future != null) {
            future.cancel(false);
        }
        if (executor != null) {
            executor.shutdown();
        }

        LOG.debug("Polling stopped");
    }

    public void onUpdate(UpdateHandler handler) {
        handlers.add(handler);
    }

    public TelegramUpdateService bind(UpdateHandler handler, String... commandAliases) {
        for (String alias : commandAliases) {
            commandHandlers.put(alias, handler);
        }
        return this;
    }
}
