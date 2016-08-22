package com.github.bumblebee.command.youtube.service;

import javax.xml.bind.JAXBException;

public interface XmlParser {

    Object getObject(String input, Class c) throws JAXBException;

    void saveObject(String output, Object o) throws JAXBException;

}
