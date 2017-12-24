package com.github.bumblebee.command.brent.meduza

import feign.RequestLine

data class MeduzaStockResponse(val usd: MeduzaStock, val eur: MeduzaStock, val brent: MeduzaStock)
data class MeduzaStock(val prev: Float, val current: Float, val state: String)

interface MeduzaApi {

    @RequestLine("GET /api/v3/stock/all")
    fun queryStock(): MeduzaStockResponse

    companion object {
        val API_ROOT = "https://meduza.io"
    }
}
