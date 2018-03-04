package com.github.bumblebee.command.currency.service;

import com.github.bumblebee.command.currency.dao.CurrencyBidRepository;
import com.github.bumblebee.command.currency.domain.CurrencyBid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyBidEvalService {

    private CurrencyBidRepository repository;

    @Autowired
    public CurrencyBidEvalService(CurrencyBidRepository repository) {
        this.repository = repository;
    }

    public List<CurrencyBid> getTodayActualBids(Long chatId) {
        Instant from = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant to = from.plus(Duration.ofDays(1));

        List<CurrencyBid> bids = repository.findByChatIdAndCreatedAtBetween(chatId,
                Date.from(from), Date.from(to));

        return bids.stream()
                .collect(Collectors.groupingBy(CurrencyBid::getOwnerId)) // group by owner
                .values().stream()
                .map(ownerBids -> // select latest bid
                        ownerBids.stream().max(Comparator.comparing(CurrencyBid::getCreatedAt)).get()
                )
                .collect(Collectors.toList());
    }

    public CurrencyBid placeBid(CurrencyBid bid) {
        return repository.save(bid);
    }

}
