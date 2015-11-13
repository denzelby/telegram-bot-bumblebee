package telegram.polling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import telegram.api.BotApi;
import telegram.domain.BasicResponse;
import telegram.domain.Update;

import java.util.List;
import java.util.Map;

class TelegramUpdateAction implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(TelegramUpdateAction.class);

    private static final int POLL_TIMEOUT_SEC = 60;
    private static final int POLL_ITEMS_BATCH_SIZE = 100;

    private final BotApi botApi;
    private final Map<String, UpdateHandler> commandHandlers;
    private final List<UpdateHandler> updateHandlers;
    private final CommandParser commandParser = new CommandParser();

    private long lastUpdateOffset;

    public TelegramUpdateAction(BotApi botApi, Map<String, UpdateHandler> commandHandlers, List<UpdateHandler> updateHandlers) {
        this.botApi = botApi;
        this.commandHandlers = commandHandlers;
        this.updateHandlers = updateHandlers;
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

        updates.stream().forEach(update -> {
            try {
                // try invoking command
                boolean consumed = invokeCommand(update);

                if (!consumed) {
                    // run handler chain if not consumed, stop if handler returns true
                    updateHandlers.stream().anyMatch(handler -> handler.onUpdate(update));
                }
            } catch (Exception e) {
                log.error("Exception during update processing", e);
            }
        });

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

    private boolean invokeCommand(Update update) {
        final String text = update.getMessage().getText();
        if (text != null && text.startsWith("/")) {
            UpdateHandler handler = commandHandlers.get(commandParser.parse(update.getMessage().getText()));
            if (handler != null) {
                handler.onUpdate(update);
                return true;
            }
        }
        return false;
    }
}
