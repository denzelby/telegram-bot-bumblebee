package com.github.bumblebee.command.brent.bitfinex

import com.github.bumblebee.util.FeignUtils
import com.google.common.base.Joiner
import feign.Client
import feign.Feign
import feign.gson.GsonDecoder
import feign.slf4j.Slf4jLogger
import org.springframework.stereotype.Service


data class BitfinexTicker(val ticker: String,
                          val bid: Double,
                          val bidSize: Double,
                          val ask: Double,
                          val askSize: Double,
                          val dailyChange: Double,
                          val dailyChangePercent: Double,
                          val lastPrice: Double,
                          val volume: Double,
                          val high: Double,
                          val low: Double)

@Service
class BitfinexProvider {

    private val client: BitfinexApi = Feign.builder()
            .decoder(GsonDecoder())
            .logLevel(feign.Logger.Level.BASIC)
            .logger(Slf4jLogger())
            .client(Client.Default(FeignUtils.allTrustSocketFactory()) { _, _ -> true })
            .target(BitfinexApi::class.java, BitfinexApi.API_ROOT)

    fun loadTickers(tickers: List<BitfinexTickerLabel>): List<BitfinexTicker> {
        val response = client.tickers(Joiner.on(",").join(tickers))
        return response.map { values ->
            BitfinexTicker(
                    values[0] as String,
                    values[1] as Double,
                    values[2] as Double,
                    values[3] as Double,
                    values[4] as Double,
                    values[5] as Double,
                    values[6] as Double,
                    values[7] as Double,
                    values[8] as Double,
                    values[9] as Double,
                    values[10] as Double
            )
        }
    }
}