package com.github.bumblebee.command.weather

import com.github.bumblebee.command.SingleArgumentCommand
import com.github.telegram.api.BotApi
import com.github.telegram.domain.Update
import org.springframework.stereotype.Component

@Component
class WeatherCommand(private val botApi: BotApi) : SingleArgumentCommand() {

    override fun handleCommand(update: Update, chatId: Long, argument: String?) {
        val operation = WeatherArgument.of(argument) ?: WeatherArgument.TEMPERATURE
        when (operation) {
            WeatherCommand.WeatherArgument.MAP_DYNAMIC -> botApi.sendPhoto(chatId, MAP_URL_DYNAMIC)
            WeatherCommand.WeatherArgument.MAP_LATEST -> botApi.sendPhoto(chatId, MAP_URL_LATEST)
            WeatherCommand.WeatherArgument.TEMPERATURE -> botApi.sendPhoto(chatId, TEMPERATURE_URL)
        }
    }

    private enum class WeatherArgument constructor(val arguments: Set<String>) {
        MAP_DYNAMIC(setOf("md", "dynamic", "dyn")),
        MAP_LATEST(setOf("ml", "latest", "lt")),
        TEMPERATURE(setOf("t", "temp"));

        companion object {
            fun of(code: String?): WeatherArgument? {
                return WeatherArgument.values().firstOrNull {
                    it.arguments.any { it.equals(code, ignoreCase = true)  }
                }
            }
        }
    }

    companion object {
        private val MAP_URL_DYNAMIC = "http://meteoinfo.by/radar/UMMN/radar-map.gif"
        private val MAP_URL_LATEST = "http://meteoinfo.by/radar/UMMN/UMMN_latest.png"
        private val TEMPERATURE_URL = "http://www.foreca.ru/meteogram.php?loc_id=100625144&lang=ru"
    }
}