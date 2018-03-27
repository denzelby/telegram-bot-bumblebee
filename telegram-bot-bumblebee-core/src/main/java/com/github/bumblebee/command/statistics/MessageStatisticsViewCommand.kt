package com.github.bumblebee.command.statistics

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.command.statistics.entity.Statistic
import com.github.bumblebee.command.statistics.service.StatisticsService
import com.github.bumblebee.service.RandomPhraseService
import com.github.bumblebee.util.logger
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
            "all" -> postStatistics(chatId, statistics.getAllStatInChatByUsers(chatId))
            else -> botApi.sendMessage(chatId, phrases.surprise())
        }
    }

    @Scheduled(cron = "0 00 21 * * *")
    fun postPeriodicStatistics() {
        log.info("Posting periodic statistics")
        statistics.getStatistics().forEach { chatId, stats ->
            val total = total(stats)
            if (total >= AUTOPOST_STAT_THRESHOLD) {
                botApi.sendMessage(chatId, render(stats, total), ParseMode.MARKDOWN)
            } else {
                log.info("Skipping posting stats to chat {}, too few messages in total", chatId)
            }
        }
    }

    private fun postStatistics(chatId: Long, stats: List<Statistic>?, filter: (Statistic) -> Boolean = { true }) {
        if (stats != null) {
            botApi.sendMessage(chatId, render(stats, total(stats), filter), ParseMode.MARKDOWN)
        } else {
            botApi.sendMessage(chatId, phrases.no())
        }
    }

    private fun postStatistics(chatId: Long, stats: Map<String?, Int>) {
        val total = stats.values.sum()
        botApi.sendMessage(chatId, render(stats, total), ParseMode.MARKDOWN)
    }

    private fun total(stats: List<Statistic>) = stats.sumBy { it.messageCount }

    private fun render(stats: Map<String?, Int>, total: Int): String {
        return stats.entries
                .sortedByDescending { it.value }
                .joinToString(
                        prefix = "Top flooders ever:\n",
                        separator = "\n",
                        transform = { "*${it.key}*: ${it.value} (${String.format("%.2f", it.value * 100.0 / total)}%)" },
                        postfix = "\n\n$total messages total"
                )
    }

    private fun render(stats: List<Statistic>, total: Int, filter: (Statistic) -> Boolean = { true }): String {
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

    companion object {
        private const val AUTOPOST_STAT_THRESHOLD = 10
        private val log = logger<MessageStatisticsViewCommand>()
    }
}