package com.github.bumblebee.command.currency.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BB_CURRENCY_BID")
public class CurrencyBid {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer chatId;
    private Integer ownerId;
    private String ownerFirstName;
    private String ownerLastName;
    private String ownerUsername;
    private Integer value;
    private Date createdAt;

    public CurrencyBid() {
    }

    public CurrencyBid(Integer chatId, Integer ownerId, String ownerFirstName, String ownerLastName, String ownerUsername, Integer value) {
        this.chatId = chatId;
        this.ownerId = ownerId;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.ownerUsername = ownerUsername;
        this.value = value;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CurrencyBid{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", ownerId=" + ownerId +
                ", ownerFirstName='" + ownerFirstName + '\'' +
                ", ownerLastName='" + ownerLastName + '\'' +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", value=" + value +
                ", createdAt=" + createdAt +
                '}';
    }
}
