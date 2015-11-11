package telegram.domain.request.keyboard;

public class ForceReply extends Keyboard {

    private final boolean forceReply = true;

    public ForceReply() {
    }

    public ForceReply(boolean selective) {
        this.selective = selective;
    }

    public boolean isForceReply() {
        return forceReply;
    }
}
