package com.github.bumblebee.command.imagesearch.provider.bingpics.response;

public class BingSearchResponse {
    BingSearchData d;

    public BingSearchData getData() {
        return d;
    }

    public void setData(BingSearchData d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "BingSearchResponse{" +
                "d=" + d +
                '}';
    }
}
