package com.olky.portfolioservice.controller;

import com.olky.portfolioservice.exception.ResourceNotFoundException;
import com.olky.portfolioservice.model.Portfolio;
import com.olky.portfolioservice.model.Holding;
import com.olky.portfolioservice.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;
@RestController
@RequestMapping("/portfolios")
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;

    //It would be better to have 'createPortfolio' as value for this API-Method!
    @PostMapping("/createPortfolio")
    public Portfolio createPortfolio(@RequestBody Portfolio portfolio) {
            return portfolioService.createPortfolio(portfolio);
    }
    @GetMapping("/{id}")
    public Optional<Portfolio> getPortfolio(@PathVariable Long id) {
            return portfolioService.getPortfolio(id);
    }
    @PostMapping("/{id}/holdings")
    public Portfolio addHolding(@PathVariable Long id, @RequestBody Holding holding) {
            return portfolioService.addHolding(id, holding);
    }
    @DeleteMapping("/{id}/holdings/{symbol}")
    public Portfolio removeHolding(@PathVariable Long id, @PathVariable String symbol) {
        return portfolioService.removeHolding(id, symbol);
    }
    @GetMapping("/{id}/valuation/{base}")
    public double getPortfolioValuation(@PathVariable Long id, @PathVariable String base) {
            return portfolioService.getPortfolioValuation(id, base);
    }
    @GetMapping("/{id}/variation")
    public double getPortfolioVariation(@PathVariable Long id, @RequestParam @DateTimeFormat(pattern = "yyyy-M-dd") LocalDate dateFrom,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-M-dd") LocalDate dateTo, @RequestParam String base) {
            return portfolioService.getPortfolioVariation(id, dateFrom, dateTo, base);
    }
}