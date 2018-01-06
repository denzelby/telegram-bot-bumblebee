package com.github.bumblebee.command.brent.bitfinex

import feign.Param
import feign.RequestLine


interface BitfinexApi {

    @RequestLine("GET /tickers?symbols={symbols}")
    fun tickers(@Param("symbols") symbols: String): List<List<Any>>

    companion object {
        const val API_ROOT = "https://api.bitfinex.com/v2"
    }
}