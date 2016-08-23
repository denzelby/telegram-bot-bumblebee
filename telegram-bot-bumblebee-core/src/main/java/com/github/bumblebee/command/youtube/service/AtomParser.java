package com.github.bumblebee.command.youtube.service;

import com.github.bumblebee.command.youtube.entity.AtomFeed;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

@Component
public class AtomParser {

    public AtomFeed getObject(String input, Class c) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(c);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        JAXBElement<AtomFeed> root = unmarshaller.unmarshal(new StreamSource(new StringReader(input)), AtomFeed.class);
        return root.getValue();
    }
}
