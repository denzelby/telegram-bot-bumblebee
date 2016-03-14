package com.github.bumblebee.command.currency;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.command.currency.domain.CurrencyBid;
import com.github.bumblebee.command.currency.service.CurrencyBidEvalService;
import com.github.bumblebee.service.RandomPhraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegram.api.BotApi;
import telegram.domain.Update;
import telegram.domain.User;

@Component
public class CurrencyPlaceBidCommand extends SingleArgumentCommand {

    private final BotApi botApi;
    private final RandomPhraseService randomPhrase;
    private final CurrencyBidEvalService bidEvalService;

    @Autowired
    public CurrencyPlaceBidCommand(BotApi botApi, RandomPhraseService randomPhrase,
                                   CurrencyBidEvalService bidEvalService) {
        this.botApi = botApi;
        this.randomPhrase = randomPhrase;
        this.bidEvalService = bidEvalService;
    }

    @Override
    public void handleCommand(Update update, Long chatId, String argument) {

        try {
            CurrencyBid bid = new CurrencyBid();
            bid.setChatId(chatId);
            bid.setValue(Integer.parseInt(argument, 10));

            User bidOwner = update.getMessage().getFrom();
            bid.setOwnerId(bidOwner.getId());
            bid.setOwnerFirstName(bidOwner.getFirstName());
            bid.setOwnerLastName(bidOwner.getLastName());
            bid.setOwnerUsername(bidOwner.getUsername());

            bidEvalService.placeBid(bid);

            botApi.sendMessage(chatId, "Bid accepted", update.getMessage().getMessageId());
        } catch (NumberFormatException e) {
            botApi.sendMessage(chatId, randomPhrase.surprise());
        }

    }
}
