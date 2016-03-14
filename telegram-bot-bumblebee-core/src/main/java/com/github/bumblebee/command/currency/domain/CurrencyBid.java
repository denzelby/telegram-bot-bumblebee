package com.github.bumblebee.command.currency.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "BB_CURRENCY_BID")
public class CurrencyBid {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Long chatId;
    private Long ownerId;
    private String ownerFirstName;
    private String ownerLastName;
    private String ownerUsername;
    private Integer value;
    private Date createdAt;

    public CurrencyBid() {
    }

    public CurrencyBid(Long chatId, Long ownerId, String ownerFirstName, String ownerLastName, String ownerUsername, Integer value) {
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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
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
