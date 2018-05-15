package com.github.bumblebee.command.brent.bitfinex


enum class BitfinexTickerLabel(val title: String, val code: String) {
    BTC("Bitcoin",  "tBTCUSD"),
    BTCASH("Bitcoin Cash", "tBCHUSD"),
    LTC("Litecoin", "tLTCUSD"),
    RIPPLE("Ripple", "tXRPUSD"),
    MONERO("Monero", "tXMRUSD"),
    DASH("Dash", "tDSHUSD"),
    ETH("Ethereum", "tETHUSD");

    override fun toString(): String = code
}