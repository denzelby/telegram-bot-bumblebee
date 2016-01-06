package com.github.bumblebee.command.currency.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BB_DAILY_RATE")
public class DailyExchangeRate {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Date date;

    private String currency;

    private Double rate;

    public DailyExchangeRate() {
    }

    public DailyExchangeRate(Date date, String currency, Double rate) {
        this.date = date;
        this.currency = currency;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "DailyExchangeRate{" +
                "id=" + id +
                ", date=" + date +
                ", currency=" + currency +
                ", rate=" + rate +
                '}';
    }
}
