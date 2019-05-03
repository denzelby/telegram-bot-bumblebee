package com.github.bumblebee.command.weather

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component

@Component
class WeatherCommand(private val botApi: BotApi) : SingleArgumentCommand() {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {
        when (WeatherArgument.of(argument) ?: WeatherArgument.TEMPERATURE) {
            WeatherArgument.MAP_DYNAMIC -> botApi.sendDocument(chatId, MAP_URL_DYNAMIC.withTimestamp())
            WeatherArgument.MAP_LATEST -> botApi.sendPhoto(chatId, MAP_URL_LATEST.withTimestamp())
            WeatherArgument.TEMPERATURE -> botApi.sendPhoto(chatId, TEMPERATURE_URL.withTimestamp())
        }
    }

    private enum class WeatherArgument constructor(val arguments: Set<String>) {
        MAP_DYNAMIC(setOf("md", "dynamic", "dyn")),
        MAP_LATEST(setOf("ml", "latest", "lt")),
        TEMPERATURE(setOf("t", "temp"));

        companion object {
            fun of(code: String?): WeatherArgument? {
                return values().firstOrNull {
                    it.arguments.any { it.equals(code, ignoreCase = true)  }
                }
            }
        }
    }

    private fun String.withTimestamp(): String =
            this + (if (contains('?')) "&" else "?" ) + "ts=" + System.currentTimeMillis()

    companion object {
        private val MAP_URL_DYNAMIC = "http://meteoinfo.by/radar/UMMN/radar-map.gif"
        private val MAP_URL_LATEST = "http://meteoinfo.by/radar/UMMN/UMMN_latest.png"
        private val TEMPERATURE_URL = "https://www.foreca.ru/meteogram.php?loc_id=100625144&lang=ru_RU"
    }
}
