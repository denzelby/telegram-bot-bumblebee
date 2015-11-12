package telegram.polling;

import telegram.domain.Update;

@FunctionalInterface
public interface UpdateHandler {

    /**
     * Handle telegram update event
     * @param update update to process
     * @return true if update is consumed and should not be propagated through handler chain
     */
    boolean onUpdate(Update update);

}
