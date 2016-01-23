package com.github.bumblebee.command.brent.meduza.response;

public class MeduzaStock {
    private Float prev;
    private Float current;
    private String state;

    public Float getPrev() {
        return prev;
    }

    public void setPrev(Float prev) {
        this.prev = prev;
    }

    public Float getCurrent() {
        return current;
    }

    public void setCurrent(Float current) {
        this.current = current;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MeduzaStock{" +
                "prev=" + prev +
                ", current=" + current +
                ", state='" + state + '\'' +
                '}';
    }
}
