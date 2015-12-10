package com.github.bumblebee.command.bingpics;

import java.util.List;

public class BingSearchResponse {
    BingSearchData d;

    public BingSearchData getData() {
        return d;
    }

    public void setData(BingSearchData d) {
        this.d = d;
    }

    public class BingSearchData {
        private List<BingSearchResult> results;

        public List<BingSearchResult> getResults() {
            return results;
        }

        public void setResults(List<BingSearchResult> results) {
            this.results = results;
        }
    }

    public class BingSearchResult {
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
    }
}
