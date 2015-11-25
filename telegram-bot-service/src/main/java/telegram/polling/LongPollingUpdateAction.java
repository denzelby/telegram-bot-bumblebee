package telegram.polling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import telegram.api.BotApi;
import telegram.domain.BasicResponse;
import telegram.domain.Update;

import java.util.List;
import java.util.function.Consumer;

class LongPollingUpdateAction implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(LongPollingUpdateAction.class);

    private static final int POLL_TIMEOUT_SEC = 60;
    private static final int POLL_ITEMS_BATCH_SIZE = 100;

    private final BotApi botApi;
    private final Consumer<Update> updateConsumer;
    private long lastUpdateOffset;

    public LongPollingUpdateAction(BotApi botApi, Consumer<Update> updateConsumer) {
        this.botApi = botApi;
        this.updateConsumer = updateConsumer;
    }

    @Override
    public void run() {

        BasicResponse<List<Update>> updateResponse = botApi.getUpdates(lastUpdateOffset, POLL_ITEMS_BATCH_SIZE, POLL_TIMEOUT_SEC);
        if (!updateResponse.isOk() || updateResponse.getResult() == null) {
            log.error("Update failed, offset = {}", lastUpdateOffset);
            updateLastUpdateOffset(updateResponse.getResult());
            return;
        }

        List<Update> updates = updateResponse.getResult();

        updates.stream().forEach(updateConsumer);

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
