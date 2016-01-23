package com.github.bumblebee.command.brent.meduza.response;

public class MeduzaStockResponse {

    private MeduzaStock usd;
    private MeduzaStock eur;
    private MeduzaStock brent;

    public MeduzaStock getUsd() {
        return usd;
    }

    public void setUsd(MeduzaStock usd) {
        this.usd = usd;
    }

    public MeduzaStock getEur() {
        return eur;
    }

    public void setEur(MeduzaStock eur) {
        this.eur = eur;
    }

    public MeduzaStock getBrent() {
        return brent;
    }

    public void setBrent(MeduzaStock brent) {
        this.brent = brent;
    }

    @Override
    public String toString() {
        return "MeduzaStockResponse{" +
                "usd=" + usd +
                ", eur=" + eur +
                ", brent=" + brent +
                '}';
    }
}
