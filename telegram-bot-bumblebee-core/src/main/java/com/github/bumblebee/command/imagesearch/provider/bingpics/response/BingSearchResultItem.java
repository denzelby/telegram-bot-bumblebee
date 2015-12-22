package com.github.bumblebee.command.imagesearch.provider.bingpics.response;

import com.github.bumblebee.command.imagesearch.domain.Image;

public class BingSearchResultItem implements Image {
    private String ContentType;
    private String MediaUrl;

    @Override
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
    public String getUrl() {
        return MediaUrl;
    }

    @Override
    public String toString() {
        return "BingSearchResultItem{" +
                "ContentType='" + ContentType + '\'' +
                ", MediaUrl='" + MediaUrl + '\'' +
                '}';
    }
}
