package com.olky.exchangerateservice.service;

import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.Map;
/**
 * Service interface for managing exchange rates.
 */
public interface ExchangeRateService {
    /**
     * Fetches the exchange rate for a given symbol (In fact for a list of given symbols. But here we handle only one symbol),
     * a given base currency, and date.
     *
     * @param symbol the cryptocurrency symbol (e.g., BTC)
     * @param base the base currency (e.g., USD)
     * @param date the date for which the exchange rate is requested
     * @return a Mono emitting a map containing the exchange rate information
     */
    Mono<Map<String, Map<String, String>>> fetchExchangeRate(String symbol, String base, LocalDate date);
}
