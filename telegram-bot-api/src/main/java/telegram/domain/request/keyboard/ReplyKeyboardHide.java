package telegram.domain.request.keyboard;

public class ReplyKeyboardHide extends Keyboard {

    private final boolean hideKeyboard = true;

    public ReplyKeyboardHide() {
    }

    public ReplyKeyboardHide(boolean selective) {
        this.selective = selective;
    }

    public boolean isHideKeyboard() {
        return hideKeyboard;
    }
}
