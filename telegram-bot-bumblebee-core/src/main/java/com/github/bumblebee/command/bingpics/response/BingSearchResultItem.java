package com.github.bumblebee.command.bingpics.response;

public class BingSearchResultItem {
    private String ContentType;
    private String MediaUrl;

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public String getMediaUrl() {
        return MediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        MediaUrl = mediaUrl;
    }

    @Override
    public String toString() {
        return "BingSearchResultItem{" +
                "ContentType='" + ContentType + '\'' +
                ", MediaUrl='" + MediaUrl + '\'' +
                '}';
    }
}
