package telegram.polling;

import telegram.domain.Update;

import java.util.Arrays;
import java.util.List;

/**
 * Update handler that reacts only on configured command
 */
public class CommandHandler implements UpdateHandler {

    private final List<String> commands;
    private final UpdateHandler commandHandler;


    public CommandHandler(UpdateHandler commandHandler, final String... commands) {

        this.commands = Arrays.asList(commands);
        this.commandHandler = commandHandler;
    }

    @Override
    public void onUpdate(Update update) {

        String text = update.getMessage().getText();
        if (text != null) {
            if (commands.stream().anyMatch(text::startsWith)) {
                commandHandler.onUpdate(update);
            }
        }
    }
}
