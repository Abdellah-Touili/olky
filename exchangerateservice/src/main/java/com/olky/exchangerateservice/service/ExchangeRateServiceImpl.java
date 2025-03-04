package com.olky.exchangerateservice.service;

import com.olky.exchangerateservice.apiCryptoResponse.CryptoPriceResponse;
import com.olky.exchangerateservice.entity.ExchangeRate;
import com.olky.exchangerateservice.exception.ExchangeRateNotFoundException;
import com.olky.exchangerateservice.exception.ExternalApiException;
import com.olky.exchangerateservice.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.*;

/**
 * Implementation of the ExchangeRateService interface.
 */
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Value("${crypto.api_url}")
    private String apiUrl;
    @Value("${crypto.api_key}")
    private String apiKey;
    private WebClient webClient;

    /**
     * Fetches the exchange rate for a given symbol, base currency, and date.
     * If the exchange rate is found in the database/repository, it returns the cached value.
     * Otherwise, it fetches the exchange rate from an external API and saves it in the repository.
     *
     * @param symbol the cryptocurrency symbol (e.g., BTC)
     * @param base the base currency (e.g., USD)
     * @param date the date for which the exchange rate is requested
     * @return a Mono emitting a map containing the exchange rate information
     */
    @Override
    public Mono<Map<String, Map<String, String>>> fetchExchangeRate(String symbol, String base, LocalDate date) {
        Optional<ExchangeRate> exchangeRateOptional=exchangeRateRepository.findBySymbolAndBaseAndDate(symbol, base, date);
        if (exchangeRateOptional.isPresent()) {
            double priceExist=exchangeRateOptional.get().getPrice();
            CryptoPriceResponse cryptoPriceResponse=new CryptoPriceResponse();
            final HashMap<String, String> basePriceMap=new HashMap<>();
            basePriceMap.put(base, String.valueOf(priceExist));
            cryptoPriceResponse.setCrypto(symbol, basePriceMap);

            return cryptoPriceResponse;

        } else {
            //To handle the buffer size memory
            ExchangeStrategies strategies=ExchangeStrategies
                    .builder()
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(1024 * 1024 * 10) // 10MB
                    )
                    .build();
            this.webClient=WebClient.builder()
                    .baseUrl(apiUrl)
                    .defaultHeader("apikey", apiKey)
                    .exchangeStrategies(strategies)
                    .build();
            return this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/simple/price")
                            .queryParam("ids", symbol)
                            .queryParam("vs_currencies", base)
                            .queryParam("date", date)
                            .build())
                    .retrieve()
                    .bodyToMono(CryptoPriceResponse.class)
                    .map((cryptoPriceResponse) -> {
                        if (cryptoPriceResponse.getCrypto() == null || !cryptoPriceResponse.getCrypto().containsKey(symbol)) {
                            throw new ExchangeRateNotFoundException("Exchange rate not found for symbol: " + symbol + " and base: " + base);
                        }
                        ExchangeRate exchangeRate=new ExchangeRate();
                        final double priceSymbolBase=Double.parseDouble(cryptoPriceResponse.getCrypto().get(symbol).get(base));
                        exchangeRate.setSymbol(symbol);
                        exchangeRate.setBase(base);
                        exchangeRate.setPrice(priceSymbolBase);
                        exchangeRate.setDate(date);
                        exchangeRateRepository.save(exchangeRate);

                        return cryptoPriceResponse.getCrypto();
                    })
                    .onErrorMap(e -> new ExternalApiException("Failed to fetch exchange rate from external API", e));
        }
    }
}