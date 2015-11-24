package telegram.polling;

import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerRegistry {

    /**
     * Command mapping
     */
    private final Map<String, UpdateHandler> commands = new HashMap<>();

    /**
     * Handler chain
     */
    private final List<UpdateHandler> handlers = new ArrayList<>();

    public HandlerRegistry register(UpdateHandler handler, String... aliases) {

        for (String alias : aliases) {
            commands.put(alias, handler);
        }
        return this;
    }

    public HandlerRegistry register(UpdateHandler handler) {
        handlers.add(handler);
        return this;
    }

    public @Nullable UpdateHandler get(String alias) {
        return commands.get(alias);
    }

    public List<UpdateHandler> getHandlerChain() {
        return handlers;
    }
}
