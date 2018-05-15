package com.github.bumblebee.command.brent.meduza

import com.github.bumblebee.util.FeignUtils
import feign.Client
import feign.Feign
import feign.gson.GsonDecoder
import feign.slf4j.Slf4jLogger
import org.springframework.stereotype.Service

@Service
class MeduzaStockProvider {

    private val client: MeduzaApi = Feign.builder()
            .decoder(GsonDecoder())
            .logLevel(feign.Logger.Level.BASIC)
            .logger(Slf4jLogger())
            .client(Client.Default(FeignUtils.allTrustSocketFactory()) { _, _ -> true })
            .target(MeduzaApi::class.java, MeduzaApi.API_ROOT)

    fun getCurrentStocks(): MeduzaStockResponse = this.client.queryStock()
}
