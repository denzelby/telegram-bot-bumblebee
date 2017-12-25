package telegram.polling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.telegram.domain.Update;

import java.time.Instant;
import java.util.function.Consumer;

public class TelegramUpdateConsumer implements Consumer<Update> {

    private static final Logger log = LoggerFactory.getLogger(TelegramUpdateConsumer.class);
    private static final int UPDATE_EXPIRATION_SEC = 2 * 60;

    private final HandlerRegistry handlerRegistry;
    private final CommandParser commandParser = new CommandParser();

    public TelegramUpdateConsumer(HandlerRegistry handlerRegistry) {
        this.handlerRegistry = handlerRegistry;
    }

    @Override
    public void accept(Update update) {
        try {
            if (isOutdatedUpdate(update)) {
                log.debug("Outdated update skipped");
                return;
            }
            // try invoking command
            boolean consumed = invokeCommand(update);

            if (!consumed) {
                // run handler chain if not consumed, stop if handler returns true
                this.handlerRegistry
                        .getHandlerChain().stream()
                        .anyMatch(handler -> handler.onUpdate(update));
            }
        } catch (Exception e) {
            log.error("Exception during update processing", e);
        }
    }

    private boolean isOutdatedUpdate(Update update) {

        final Integer unixTime = (update.getMessage() != null) ? update.getMessage().getDate() : null;
        return unixTime != null && Instant.ofEpochSecond(unixTime).isBefore(
                Instant.now().minusSeconds(UPDATE_EXPIRATION_SEC));
    }

    private boolean invokeCommand(Update update) {
        final String text = (update.getMessage() != null) ? update.getMessage().getText() : null;
        if (text != null && text.startsWith("/")) {
            UpdateHandler handler = handlerRegistry.get(commandParser.parse(text));
            if (handler != null) {
                handler.onUpdate(update);
                return true;
            }
        }
        return false;
    }

}
