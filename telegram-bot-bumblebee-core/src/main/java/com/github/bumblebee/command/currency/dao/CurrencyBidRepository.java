package com.github.bumblebee.command.currency.dao;


import com.github.bumblebee.command.currency.domain.CurrencyBid;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CurrencyBidRepository extends CrudRepository<CurrencyBid, Long> {

    List<CurrencyBid> findByChatIdAndCreatedAtBetween(Integer chatId, Date from, Date to);
}
