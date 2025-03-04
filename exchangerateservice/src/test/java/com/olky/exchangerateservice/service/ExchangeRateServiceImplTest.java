package com.olky.exchangerateservice.service;

import com.olky.exchangerateservice.entity.ExchangeRate;
import com.olky.exchangerateservice.repository.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
public class ExchangeRateServiceImplTest {
    @Mock
    private ExchangeRateRepository exchangeRateRepository;
    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testFetchExchangeRate() {
        String symbol = "BTC";
        String base = "USD";
        LocalDate date = LocalDate.now();
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setSymbol(symbol);
        exchangeRate.setBase(base);
        exchangeRate.setPrice(50000.0);
        exchangeRate.setDate(date);

        when(exchangeRateRepository.findBySymbolAndBaseAndDate(symbol, base, date))
                .thenReturn(Optional.of(exchangeRate));

        Mono<Map<String, Map<String, String>>> result = exchangeRateService.fetchExchangeRate(symbol, base, date);

        assertNotNull(result.block());
    }
}

