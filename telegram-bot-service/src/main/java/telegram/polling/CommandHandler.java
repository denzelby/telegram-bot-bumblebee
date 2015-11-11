package telegram.polling;

import telegram.domain.Update;

/**
 * Update handler that reacts only on configured command
 */
public class CommandHandler implements UpdateHandler {

    private final String command;
    private final UpdateHandler commandHandler;

    public CommandHandler(final String command, UpdateHandler commandHandler) {

        this.command = command;
        this.commandHandler = commandHandler;
    }

    @Override
    public void onUpdate(Update update) {

        String text = update.getMessage().getText();
        if (text != null && text.startsWith(command)) {
            commandHandler.onUpdate(update);
        }
    }
}
