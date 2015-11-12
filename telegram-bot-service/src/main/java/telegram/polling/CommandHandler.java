package telegram.polling;

import java.util.Arrays;
import java.util.List;

/**
 * Update handler that reacts only on configured command
 */
public class CommandHandler {

    private final List<String> commands;
    private final UpdateHandler commandHandler;

    public CommandHandler(UpdateHandler commandHandler, final String... commands) {

        this.commands = Arrays.asList(commands);
        this.commandHandler = commandHandler;
    }

    public List<String> getCommands() {
        return commands;
    }

    public UpdateHandler getCommandHandler() {
        return commandHandler;
    }


}
