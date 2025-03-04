package com.olky.exchangerateservice.controller;

import com.olky.exchangerateservice.exception.ExchangeRateNotFoundException;
import com.olky.exchangerateservice.exception.ExternalApiException;
import com.olky.exchangerateservice.service.ExchangeRateService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Map;
@RestController
@RequestMapping("/crypto")
public class ExchangeRateController {
    @Autowired
    private ExchangeRateService exchangeRateService;
    @RequestMapping(value="/exchange-rate", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public Mono<Map<String, Map<String, String>>> fetchExchangeRate(@RequestParam("symbol") String symbol, @RequestParam("base") String base,
                                                                    @Nullable @RequestParam("date") @DateTimeFormat(pattern = "yyyy-M-dd") LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return exchangeRateService.fetchExchangeRate(symbol, base, date);
         }
}