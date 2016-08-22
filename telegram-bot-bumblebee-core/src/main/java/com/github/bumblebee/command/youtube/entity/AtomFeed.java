package com.github.bumblebee.command.youtube.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "feed")
@XmlAccessorType(XmlAccessType.FIELD)
public class AtomFeed {


    @XmlElement(name = "entry", type = AtomFeedEntry.class)
    private AtomFeedEntry entry;


    public AtomFeedEntry getEntry() {
        return entry;
    }

    public void setEntry(AtomFeedEntry entry) {
        this.entry = entry;
    }
}
