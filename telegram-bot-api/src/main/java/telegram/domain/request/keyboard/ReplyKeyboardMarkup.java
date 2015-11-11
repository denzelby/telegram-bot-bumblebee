package telegram.domain.request.keyboard;

public class ReplyKeyboardMarkup extends Keyboard {

    private final String[][] keyboard;
    private boolean resizeKeyboard;
    private boolean oneTimeKeyboard;

    public ReplyKeyboardMarkup(String[]... keyboard) {
        this(keyboard, false, false, false);
    }

    public ReplyKeyboardMarkup(String[][] keyboard, boolean resizeKeyboard, boolean oneTimeKeyboard, boolean selective) {
        this.keyboard = keyboard;
        this.resizeKeyboard = resizeKeyboard;
        this.oneTimeKeyboard = oneTimeKeyboard;
        this.selective = selective;
    }

    public String[][] getKeyboard() {
        return keyboard;
    }

    public boolean isResizeKeyboard() {
        return resizeKeyboard;
    }

    public boolean isOneTimeKeyboard() {
        return oneTimeKeyboard;
    }
}
