package com.github.bumblebee.command.currency.dataprovider.nbrb;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class NBRBExRatesParser {

    private static final String API_URL = "http://nbrb.by/Services/XmlExRates.aspx?ondate=";
    private static final String NUM_CODE = "NumCode";
    private static final String NAME = "Name";
    private static final String CHAR_CODE = "CharCode";
    private static final String SCALE = "Scale";
    private static final String RATE = "Rate";
    private static final String CURRENCY = "Currency";

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private DocumentBuilder documentBuilder;

    public NBRBExRatesParser() {
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Currency> getDailyRates() throws IOException, SAXException {
        return getDailyRates(null);
    }

    public List<Currency> getDailyRates(LocalDate date) throws IOException, SAXException {

        Document doc = documentBuilder.parse(getRequestUrl(date));

        List<Currency> currencies = new ArrayList<>();
        NodeList list = doc.getElementsByTagName(CURRENCY);
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                currencies.add(convert((Element) node));
            }
        }
        return currencies;
    }

    private String getRequestUrl(LocalDate date) {
        return (date != null) ? API_URL + date.format(dateFormatter) : API_URL;
    }

    private Currency convert(Element currencyElement) {
        Currency currency = new Currency();
        currency.setCode(getValue(NUM_CODE, currencyElement));
        currency.setName(getValue(NAME, currencyElement));
        currency.setShortName(getValue(CHAR_CODE, currencyElement));
        currency.setScale(Integer.parseInt(getValue(SCALE, currencyElement)));
        currency.setRate(Double.parseDouble(getValue(RATE, currencyElement)));
        return currency;
    }

    private String getValue(String tagName, Element e) {
        String value = null;

        NodeList list = e.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList childList = list.item(0).getChildNodes();
            if (childList != null && childList.getLength() > 0) {
                value = childList.item(0).getNodeValue();
            }
        }

        return value;
    }

}
