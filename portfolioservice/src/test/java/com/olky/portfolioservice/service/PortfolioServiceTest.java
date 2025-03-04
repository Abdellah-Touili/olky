package com.olky.portfolioservice.service;

import com.olky.portfolioservice.model.Portfolio;
import com.olky.portfolioservice.model.Holding;
import com.olky.portfolioservice.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PortfolioServiceTest {
    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private PortfolioService portfolioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePortfolio() {
        Portfolio portfolio = new Portfolio();
        portfolio.setName("Test Portfolio");

        when(portfolioRepository.save(portfolio)).thenReturn(portfolio);

        Portfolio createdPortfolio = portfolioService.createPortfolio(portfolio);
        assertNotNull(createdPortfolio);
        assertEquals("Test Portfolio", createdPortfolio.getName());
    }

    @Test
    public void testGetPortfolioValuation() {
        Portfolio portfolio = new Portfolio();
        portfolio.setId(1L);
        Holding holding = new Holding();
        holding.setSymbol("BTC");
        holding.setAmount(1.0);
        portfolio.setHoldings(List.of(holding));

        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));
        when(exchangeRateService.getExchangeRate("BTC", "USD", null)).thenReturn(50000.0);

        double valuation = portfolioService.getPortfolioValuation(1L, "USD");
        assertEquals(50000.0, valuation);
    }

    @Test
    public void testGetPortfolioVariation() {
        Portfolio portfolio = new Portfolio();
        portfolio.setId(1L);
        Holding holding = new Holding();
        holding.setSymbol("BTC");
        holding.setAmount(1.0);
        portfolio.setHoldings(List.of(holding));

        LocalDate dateFrom = LocalDate.parse("2024-01-01");
        LocalDate dateTo = LocalDate.parse("2024-12-31");


        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));
        when(portfolioService.getHistoricalValuation(portfolio, dateFrom,"USD")).thenReturn(40000.0);
        when(portfolioService.getHistoricalValuation(portfolio, dateTo, "USD")).thenReturn(60000.0);

        double variation = portfolioService.getPortfolioVariation(1L, dateFrom, dateTo, "USD");
        assertEquals(50.0, variation);
    }
}
