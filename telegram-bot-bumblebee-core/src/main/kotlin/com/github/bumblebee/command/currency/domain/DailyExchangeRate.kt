package com.github.bumblebee.command.currency.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "BB_DAILY_RATE")
class DailyExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    lateinit var date: Date
    lateinit var currency: String
    var rate: Double = 0.0

    @Suppress("unused")
    constructor()

    constructor(date: Date, currency: String, rate: Double) {
        this.date = date
        this.currency = currency
        this.rate = rate
    }

    override fun toString(): String {
        return "DailyExchangeRate(id=$id, date=$date, currency=$currency, rate=$rate)"
    }
}
