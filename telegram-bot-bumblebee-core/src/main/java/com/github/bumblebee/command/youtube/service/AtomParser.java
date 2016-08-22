package com.github.bumblebee.command.youtube.service;

import com.github.bumblebee.command.youtube.entity.AtomFeed;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

public class AtomParser implements XmlParser {

    @Override
    public Object getObject(String input, Class c) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(c);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        JAXBElement<AtomFeed> root = unmarshaller.unmarshal(new StreamSource(new StringReader(input)), AtomFeed.class);
        return root.getValue();
    }

    @Override
    public void saveObject(String output, Object o) throws JAXBException {

    }
}
