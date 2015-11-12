package com.github.bumblebee.command.googlepics;

import java.util.List;

public class GooglePicsResponse {

    private ResponseData responseData;

    public GooglePicsResponse(ResponseData responseData) {
        this.responseData = responseData;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public class ResponseData {
        List<PictureResult> results;

        public ResponseData(List<PictureResult> results) {
            this.results = results;
        }

        public List<PictureResult> getResults() {
            return results;
        }
    }

    public class PictureResult {

        private String url;

        public PictureResult(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
