package com.github.bumblebee.command.brent.meduza;

import com.github.bumblebee.command.brent.meduza.response.MeduzaStockResponse;
import feign.RequestLine;

public interface MeduzaApi {

    String API_ROOT = "https://meduza.io";

    @RequestLine("GET /api/v3/stock/all")
    MeduzaStockResponse queryStock();
}
