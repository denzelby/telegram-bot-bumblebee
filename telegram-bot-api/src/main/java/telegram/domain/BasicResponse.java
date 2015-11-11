package telegram.domain;

public class BasicResponse<T> {

    private Boolean ok;
    private T result;

    public Boolean isOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "BasicResponse{" +
                "ok=" + ok +
                ", result=" + result +
                '}';
    }
}
