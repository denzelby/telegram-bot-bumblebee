package telegram.domain;

public class SetWebHookResponse extends BasicResponse<Boolean> {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
