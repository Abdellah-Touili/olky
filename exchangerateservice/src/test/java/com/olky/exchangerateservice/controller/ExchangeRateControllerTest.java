package com.olky.exchangerateservice.controller;

import com.olky.exchangerateservice.service.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ExchangeRateControllerTest {


    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ExchangeRateController exchangeRateController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchExchangeRate() {
        String symbol = "BTC";
        String base = "USD";
        LocalDate date = LocalDate.now();
        Map<String, Map<String, String>> mockResponse = Map.of("BTC", Map.of("USD", "50000"));

        when(exchangeRateService.fetchExchangeRate(symbol, base, date)).thenReturn(Mono.just(mockResponse));

        Mono<Map<String, Map<String, String>>> response = exchangeRateController.fetchExchangeRate(symbol, base, date);

        assertEquals(mockResponse, response.block());
    }
}
