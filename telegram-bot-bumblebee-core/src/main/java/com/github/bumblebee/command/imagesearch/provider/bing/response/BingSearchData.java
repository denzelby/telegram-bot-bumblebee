package com.github.bumblebee.command.imagesearch.provider.bing.response;

import java.util.List;

public class BingSearchData {
    private List<BingSearchResultItem> results;

    public List<BingSearchResultItem> getResults() {
        return results;
    }

    public void setResults(List<BingSearchResultItem> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "BingSearchData{" +
                "results=" + results +
                '}';
    }
}
