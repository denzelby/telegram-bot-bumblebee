package com.github.bumblebee.command.currency.dao;


import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.bumblebee.command.currency.domain.CurrencyBid;

@Repository
public interface CurrencyBidRepository extends CrudRepository<CurrencyBid, Long> {

    List<CurrencyBid> findByChatIdAndCreatedAtBetween(Long chatId, Date from, Date to);
}
