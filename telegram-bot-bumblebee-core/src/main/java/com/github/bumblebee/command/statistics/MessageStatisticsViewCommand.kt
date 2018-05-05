package com.github.bumblebee.command.statistics

import com.github.bumblebee.command.CallbackAware
import com.github.bumblebee.command.SingleArgumentCommand
import com.github.bumblebee.command.statistics.entity.Statistic
import com.github.bumblebee.command.statistics.service.StatisticsChartService
import com.github.bumblebee.command.statistics.service.StatisticsService
import com.github.bumblebee.service.RandomPhraseService
import com.github.bumblebee.util.logger
import com.github.telegram.api.BotApi
import com.github.telegram.api.InputFile
import com.github.telegram.domain.*
import org.springframework.context.annotation.DependsOn
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.time.DayOfWeek
import java.time.LocalDate

@Component
@DependsOn("timeZoneConfig")
class MessageStatisticsViewCommand(
    private val botApi: BotApi,
    private val statistics: StatisticsService,
    private val chartService: StatisticsChartService,
    private val phrases: RandomPhraseService
) : SingleArgumentCommand(), CallbackAware {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {
        when (argument) {
            null -> postStatistics(chatId, statistics.getStatistics()[chatId])
            "me" -> postStatistics(chatId, statistics.getStatistics()[chatId]) { stat ->
                stat.authorId == update.message?.from?.id
            }
            "week", "w" -> weeklyStatistics(chatId, LocalDate.now())
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
                transform = {
                    "*${it.authorName}*: ${it.messageCount} (${String.format("%.2f", it.messageCount * 100.0 / total)}%)"
                },
                postfix = "\n\n$total messages total"
            )
    }

    private fun getWeekRange(date: LocalDate): Pair<LocalDate, LocalDate> {
        val weekStart = date.minusDays((date.dayOfWeek.value - DayOfWeek.MONDAY.value).toLong())
        val weekEnd = date.plusDays((DayOfWeek.SUNDAY.value - date.dayOfWeek.value).toLong())
        return (weekStart to weekEnd)
    }

    private fun weeklyStatistics(chatId: Long, date: LocalDate) {
        botApi.sendChatAction(chatId, ChatAction.UPLOAD_PHOTO)

        val (weekStart, weekEnd) = getWeekRange(date)
        val image = chartService.getWeeklyStats(chatId, weekStart, weekEnd)
        if (image.isPresent) {
            val keyboard = createStatsSelectorButtons(weekStart)
            val photo = InputFile.photo(ByteArrayInputStream(image.get()), "image")
            botApi.sendPhoto(chatId, photo, null, null, null, keyboard)
        } else botApi.sendMessage(chatId, "There are no more data")
    }

    private fun createStatsSelectorButtons(weekStart: LocalDate): ReplyMarkup {
        return InlineKeyboardReplyMarkup(
            listOf(
                listOf(
                    InlineKeyboardButton(
                        text = "Previous week",
                        callbackData = callbackData(weekStart.minusWeeks(1).toString())
                    )
                )
            )
        )
    }

    override fun callbackId(): String = "stats"

    override fun onCallback(data: String, callbackQuery: CallbackQuery) {
        callbackQuery.message?.let { message ->
            val chatId = message.chat.id
            log.debug("Callback query received: $data")
            botApi.editMessageReplyMarkup(chatId, message.messageId, ReplyMarkup.empty())
            weeklyStatistics(chatId, LocalDate.parse(data))
        }
        botApi.answerCallbackQuery(callbackQuery.id, "Done!", false)
    }

    companion object {
        private const val AUTOPOST_STAT_THRESHOLD = 10
        private val log = logger<MessageStatisticsViewCommand>()
    }
}