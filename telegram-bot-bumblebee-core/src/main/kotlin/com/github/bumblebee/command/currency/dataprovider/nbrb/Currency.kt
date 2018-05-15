package com.github.bumblebee.command.currency.dataprovider.nbrb

data class Currency(
        val name: String,
        val shortName: String,
        val code: String,
        val scale: Int,
        val rate: Double
)
