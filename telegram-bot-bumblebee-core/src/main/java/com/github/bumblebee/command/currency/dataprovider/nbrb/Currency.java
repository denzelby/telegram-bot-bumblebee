package com.github.bumblebee.command.currency.dataprovider.nbrb;

public class Currency {
    private String name;
    private String shortName;
    private String code;
    private int scale;
    private double rate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int amount) {
        this.scale = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "SupportedCurrency{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", code=" + code +
                ", scale=" + scale +
                ", rate=" + rate +
                '}';
    }
}
