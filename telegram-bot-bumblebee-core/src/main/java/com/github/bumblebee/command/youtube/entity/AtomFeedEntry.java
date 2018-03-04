package com.github.bumblebee.command.youtube.entity;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.FIELD)
public class AtomFeedEntry {

    @XmlElement
    private String videoId;

    @XmlElement
    private String channelId;

    @XmlElement
    private Date updated;

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "AtomFeedEntry{" +
                "videoId='" + videoId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", updated=" + updated +
                '}';
    }
}
