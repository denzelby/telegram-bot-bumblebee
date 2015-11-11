package telegram.domain.request;

public enum ParseMode {

    DEFAULT(null), MARKDOWN("Markdown");

    private final String value;

    ParseMode(String value) {

        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
