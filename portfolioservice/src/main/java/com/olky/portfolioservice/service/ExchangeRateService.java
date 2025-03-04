package com.olky.portfolioservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.olky.portfolioservice.exception.ExchangeRateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.time.LocalDate;

@Service
public class ExchangeRateService {
    private WebClient webClient;
    @Value("${portfolio.api_local_url}")
    private String apiPortfolioUrl;
    /**
     *
     * The method call an external web service via a Rest-API to get the current price for a specific crypto symbol and a specific date
     *
     * @param symbol the symbol of the cryptocurrency to get its price
     * @param baseCurrency the given base currency
     * @param date the date for the exchange rate (can be null for current rate)
     * @return the price
     * @throws ExchangeRateException if an error occurs while retrieving the exchange rate
     */
    public double getExchangeRate(String symbol, String baseCurrency, LocalDate date) {
        this.webClient=WebClient.builder()
                .baseUrl(apiPortfolioUrl)
                .build();
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/exchange-rate")
                            .queryParam("symbol", symbol)
                            .queryParam("base", baseCurrency)
                            .queryParam("date", date)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .map(jsonNode -> Double.valueOf(jsonNode.get(symbol).get(baseCurrency).asText()))
                    .block();
        } catch (WebClientResponseException e) {
            throw new ExchangeRateException("Failed to retrieve exchange rate from external service", e);
        } catch (Exception e) {
            throw new ExchangeRateException("An unexpected error occurred while retrieving exchange rate", e);
        }
    }
}