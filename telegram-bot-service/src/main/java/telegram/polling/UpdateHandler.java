package telegram.polling;

import telegram.domain.Update;

@FunctionalInterface
public interface UpdateHandler {

    void onUpdate(Update update);
}
