package telegram.polling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TelegramUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(TelegramUpdateService.class);

    private ScheduledExecutorService executor;
    private Future<?> future;
    private final Runnable updateAction;

    private final int pollInterval;
    private final TimeUnit pollIntervalUnit;

    public TelegramUpdateService(Runnable updateAction) {
        this(updateAction, 1, TimeUnit.SECONDS);
    }

    public TelegramUpdateService( Runnable updateAction, int pollInterval, TimeUnit timeUnit) {
        this.updateAction = updateAction;
        this.pollInterval = pollInterval;
        this.pollIntervalUnit = timeUnit;
    }

    public synchronized void startPolling() {

        if (executor == null) {
            executor = Executors.newSingleThreadScheduledExecutor();
        }

        future = executor.scheduleWithFixedDelay(updateAction, 0, pollInterval, pollIntervalUnit);

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

}
