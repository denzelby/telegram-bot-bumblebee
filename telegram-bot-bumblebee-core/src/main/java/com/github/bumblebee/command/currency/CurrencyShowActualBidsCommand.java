package com.github.bumblebee.command.currency;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.command.currency.domain.CurrencyBid;
import com.github.bumblebee.command.currency.service.CurrencyBidEvalService;
import com.github.bumblebee.service.RandomPhraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.github.telegram.api.BotApi;
import com.github.telegram.domain.Update;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrencyShowActualBidsCommand extends SingleArgumentCommand {

    private final BotApi botApi;
    private final RandomPhraseService randomPhrase;
    private final CurrencyBidEvalService bidEvalService;
    private final DecimalFormat numberFormat = new DecimalFormat("+#,##0;-#");

    @Autowired
    public CurrencyShowActualBidsCommand(BotApi botApi, RandomPhraseService randomPhrase,
                                         CurrencyBidEvalService bidEvalService) {
        this.botApi = botApi;
        this.randomPhrase = randomPhrase;
        this.bidEvalService = bidEvalService;
    }

    @Override
    public void handleCommand(Update update, long chatId, String argument) {

        List<CurrencyBid> actualBids = bidEvalService.getTodayActualBids(chatId);
        actualBids.sort(Comparator.comparing(CurrencyBid::getValue).reversed());

        botApi.sendMessage(chatId, format(actualBids));
    }

    private String format(List<CurrencyBid> bids) {
        if (!bids.isEmpty()) {
            return bids.stream()
                    .map(bid -> ownerOf(bid) + ": " + numberFormat.format(bid.getValue()))
                    .collect(Collectors.joining("\n"));
        }
        return "No bids for today.";
    }

    private String ownerOf(CurrencyBid bid) {
        StringBuilder sb = new StringBuilder(bid.getOwnerFirstName());
        if (!StringUtils.isEmpty(bid.getOwnerLastName())) {
            sb.append(" ");
            sb.append(bid.getOwnerLastName());
        }
        return sb.toString();
    }
}
