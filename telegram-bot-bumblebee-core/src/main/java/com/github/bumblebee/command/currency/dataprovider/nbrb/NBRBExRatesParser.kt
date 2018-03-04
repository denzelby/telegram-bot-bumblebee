package com.github.bumblebee.command.currency.dataprovider.nbrb

import org.springframework.stereotype.Component
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.SAXException
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

@Component
class NBRBExRatesParser {

    private val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    private val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()

    fun getDailyRates(): List<Currency> = getDailyRates(null)

    @Throws(IOException::class, SAXException::class)
    fun getDailyRates(date: LocalDate?): List<Currency> {
        val doc = documentBuilder.parse(getRequestUrl(date))

        val list = doc.getElementsByTagName(CURRENCY)
        return (0 until list.length).asSequence()
                .map { list.item(it) }
                .filter { it.nodeType == Node.ELEMENT_NODE }
                .map { convert(it as Element) }
                .toList()
    }

    private fun getRequestUrl(date: LocalDate?): String {
        return if (date != null) API_URL + date.format(dateFormatter) else API_URL
    }

    private fun convert(currencyElement: Element): Currency {
        return Currency(
                code = getValue(NUM_CODE, currencyElement),
                name = getValue(NAME, currencyElement),
                shortName = getValue(CHAR_CODE, currencyElement),
                scale = getValue(SCALE, currencyElement).toInt(),
                rate = getValue(RATE, currencyElement).toDouble()
        )
    }

    private fun getValue(tagName: String, e: Element): String {
        val list = e.getElementsByTagName(tagName)
        if (list != null && list.length > 0) {
            val childList = list.item(0).childNodes
            if (childList != null && childList.length > 0) {
                return childList.item(0).nodeValue
            }
        }
        throw IllegalStateException("Tag not found: $tagName")
    }

    companion object {
        private val API_URL = "http://nbrb.by/Services/XmlExRates.aspx?ondate="
        private val NUM_CODE = "NumCode"
        private val NAME = "Name"
        private val CHAR_CODE = "CharCode"
        private val SCALE = "Scale"
        private val RATE = "Rate"
        private val CURRENCY = "Currency"
    }
}
