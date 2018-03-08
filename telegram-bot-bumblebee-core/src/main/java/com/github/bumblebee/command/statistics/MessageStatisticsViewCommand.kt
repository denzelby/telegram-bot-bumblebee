package com.github.bumblebee.command.statistics

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.command.statistics.entity.Statistic
import com.github.bumblebee.command.statistics.service.StatisticsService
import com.github.bumblebee.service.RandomPhraseService
import com.github.telegram.api.BotApi
import com.github.telegram.domain.ParseMode
import com.github.telegram.domain.Update
import org.springframework.context.annotation.DependsOn
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@DependsOn("timeZoneConfig")
class MessageStatisticsViewCommand(private val botApi: BotApi,
                                   private val statistics: StatisticsService,
                                   private val phrases: RandomPhraseService) : SingleArgumentCommand() {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {
        when (argument) {
            null -> postStatistics(chatId, statistics.getStatistics()[chatId])
            "me" -> postStatistics(chatId, statistics.getStatistics()[chatId]) { stat ->
                stat.authorId == update.message?.from?.id
            }
            else -> botApi.sendMessage(chatId, phrases.surprise())
        }
    }

    @Scheduled(cron = "0 00 21 * * *")
    fun postPeriodicStatistics() {
        statistics.getStatistics().forEach { chatId, stats ->
            postStatistics(chatId, stats)
        }
    }

    private fun postStatistics(chatId: Long, stats: List<Statistic>?, filter: (Statistic) -> Boolean = { true }) {
        if (stats != null)
            botApi.sendMessage(chatId, render(stats, filter), ParseMode.MARKDOWN)
        else
            botApi.sendMessage(chatId, phrases.no())
    }

    private fun render(stats: List<Statistic>, filter: (Statistic) -> Boolean = { true }): String {
        val total = stats.sumBy { it.messageCount }
        return stats
                .filter(filter)
                .sortedByDescending { it.messageCount }
                .joinToString(
                        prefix = "Top flooders of the day:\n",
                        separator = "\n",
                        transform = { "*${it.authorName}*: ${it.messageCount} (${String.format("%.2f", it.messageCount * 100.0 / total)}%)" },
                        postfix = "\n\n$total messages total"
                )
    }
}