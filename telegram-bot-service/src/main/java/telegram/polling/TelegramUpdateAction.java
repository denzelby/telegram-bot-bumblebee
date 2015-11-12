package telegram.polling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import telegram.api.BotApi;
import telegram.domain.BasicResponse;
import telegram.domain.Update;

import java.util.List;

class TelegramUpdateAction implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(TelegramUpdateAction.class);

    public static final int POLL_TIMEOUT_SEC = 60;
    public static final int POLL_ITEMS_BATCH_SIZE = 100;

    private final BotApi botApi;
    private final List<UpdateHandler> handlers;

    private long lastUpdateOffset;

    public TelegramUpdateAction(BotApi botApi, List<UpdateHandler> handlers) {
        this.botApi = botApi;
        this.handlers = handlers;
    }

    @Override
    public void run() {

        BasicResponse<List<Update>> updateResponse = botApi.getUpdates(lastUpdateOffset, POLL_ITEMS_BATCH_SIZE, POLL_TIMEOUT_SEC);
        if (!updateResponse.isOk() || updateResponse.getResult() == null) {
            log.error("Update failed, offset = {}", lastUpdateOffset);
            return;
        }

        List<Update> updates = updateResponse.getResult();

        updates.stream().forEach(u -> handlers.forEach(handler -> handler.onUpdate(u)));

        // assuming that last update have highest offset (?)
        // todo: verify
        if (updates.size() > 0) {
            Update lastUpdate = updates.get(updates.size() - 1);
            log.debug("offset: {} -> {}", lastUpdateOffset, lastUpdate.getUpdateId() + 1);
            lastUpdateOffset = lastUpdate.getUpdateId() + 1;
        }
    }
}
