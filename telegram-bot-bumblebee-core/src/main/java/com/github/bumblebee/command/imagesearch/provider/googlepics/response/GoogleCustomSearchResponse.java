package com.github.bumblebee.command.imagesearch.provider.googlepics.response;

import java.util.List;

public class GoogleCustomSearchResponse {

    private List<GoogleSearchItem> items;

    public List<GoogleSearchItem> getItems() {
        return items;
    }

    public void setItems(List<GoogleSearchItem> items) {
        this.items = items;
    }
}
