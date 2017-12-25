package telegram.polling;

import com.github.telegram.api.BotApi;
import com.github.telegram.api.Response;
import com.github.telegram.domain.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

class LongPollingUpdateAction implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(LongPollingUpdateAction.class);

    private static final int POLL_TIMEOUT_SEC = 60;
    private static final int POLL_ITEMS_BATCH_SIZE = 100;

    private final BotApi botApi;
    private final Consumer<Update> updateConsumer;
    private long lastUpdateOffset;

    LongPollingUpdateAction(BotApi botApi, Consumer<Update> updateConsumer) {
        this.botApi = botApi;
        this.updateConsumer = updateConsumer;
    }

    @Override
    public void run() {

        try {
            Response<List<Update>> response = botApi
                    .getUpdates(lastUpdateOffset, POLL_ITEMS_BATCH_SIZE, POLL_TIMEOUT_SEC);

                processUpdates(response);
        } catch (RuntimeException e) {
            log.error("Failed to call telegram api", e);
        }
    }

    public void processUpdates(Response<List<Update>> updateResponse) {
        if (!updateResponse.getOk() || updateResponse.getResult() == null) {
            log.error("Update failed, offset = {}", lastUpdateOffset);
            updateLastUpdateOffset(updateResponse.getResult());
            return;
        }

        List<Update> updates = updateResponse.getResult();

        updates.forEach(updateConsumer);

        // assuming that last update have highest offset (?)
        // todo: verify
        if (updates.size() > 0) {
            updateLastUpdateOffset(updates);
        }
    }

    private void updateLastUpdateOffset(List<Update> updates) {
        if (updates != null && updates.size() > 0) {
            Update lastUpdate = updates.get(updates.size() - 1);
            log.trace("offset: {} -> {}", lastUpdateOffset, lastUpdate.getUpdateId() + 1);
            lastUpdateOffset = lastUpdate.getUpdateId() + 1;
        }
    }

}
