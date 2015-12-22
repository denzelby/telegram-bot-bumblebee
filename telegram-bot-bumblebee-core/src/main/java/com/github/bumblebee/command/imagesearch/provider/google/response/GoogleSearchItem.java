package com.github.bumblebee.command.imagesearch.provider.google.response;

import com.github.bumblebee.command.imagesearch.domain.Image;

public class GoogleSearchItem implements Image {
    private String link;
    private String mime;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    @Override
    public String getUrl() {
        return link;
    }

    @Override
    public String getContentType() {
        return mime;
    }
}
