package com.github.bumblebee.command.currency.service;

import com.github.bumblebee.command.currency.dao.CurrencyBidRepository;
import com.github.bumblebee.command.currency.domain.CurrencyBid;
import com.google.api.client.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyBidEvalServiceTest {

    @Mock
    private CurrencyBidRepository repository;

    @InjectMocks
    private CurrencyBidEvalService service;

    @Test
    public void testGetTodayActualBids() throws Exception {

        // given
        ZonedDateTime today = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime tomorrow = today.plusDays(1);

        List<CurrencyBid> bids = Lists.newArrayList();

        CurrencyBid bid1 = new CurrencyBid(412L, 100L, "A", "aaa", "@a", 20);
        bid1.setCreatedAt(Date.from(today.toInstant()));
        bids.add(bid1);

        CurrencyBid bid2 = new CurrencyBid(412L, 100L, "A", "aaa", "@a", -30);
        bid2.setCreatedAt(Date.from(today.plusHours(4).toInstant()));
        bids.add(bid2);

        CurrencyBid bid4 = new CurrencyBid(412L, 200L, "B", "bbb", "@b", 42);
        bid4.setCreatedAt(Date.from(today.plusHours(3).toInstant()));
        bids.add(bid4);
        when(repository.findByChatIdAndCreatedAtBetween(42L,
                Date.from(today.toInstant()), Date.from(tomorrow.toInstant()))
        ).thenReturn(bids);

        // when
        List<CurrencyBid> actualBids = service.getTodayActualBids(42L);

        // then
        assertEquals(2, actualBids.size());
        assertEquals((Integer) (-30), actualBids.get(0).getValue());
        assertEquals((Integer) 42, actualBids.get(1).getValue());
    }
}