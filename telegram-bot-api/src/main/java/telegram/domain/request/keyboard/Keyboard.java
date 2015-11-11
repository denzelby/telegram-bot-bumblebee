package telegram.domain.request.keyboard;

public abstract class Keyboard {

    protected Boolean selective;

    public Boolean isSelective() {
        return selective;
    }
}
