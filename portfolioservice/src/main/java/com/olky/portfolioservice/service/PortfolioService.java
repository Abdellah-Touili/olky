package com.olky.portfolioservice.service;

import com.olky.portfolioservice.exception.ExchangeRateException;
import com.olky.portfolioservice.exception.ResourceNotFoundException;
import com.olky.portfolioservice.model.Portfolio;
import com.olky.portfolioservice.model.Holding;
import com.olky.portfolioservice.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;
@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private ExchangeRateService exchangeRateService;
    /**
     * Creates a new portfolio.
     *
     * @param portfolio the portfolio to create
     * @return the created portfolio
     */
    public Portfolio createPortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }
    /**
     * Retrieves a portfolio by its ID.
     *
     * @param id the ID of the portfolio
     * @return an Optional containing the portfolio if found, or empty if not found
     */
    public Optional<Portfolio> getPortfolio(Long id) {
        return Optional.ofNullable(portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id " + id)));
    }
    /**
     * Adds a holding to a portfolio.
     *
     * @param portfolioId the ID of the portfolio
     * @param holding the holding to add
     * @return the updated portfolio
     * @throws ResourceNotFoundException if the portfolio is not found
     */
    public Portfolio addHolding(Long portfolioId, Holding holding) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id " + portfolioId));
        holding.setPortfolio(portfolio);
        portfolio.getHoldings().add(holding);
        return portfolioRepository.save(portfolio);
    }
    /**
     * Removes a holding from a portfolio.
     *
     * @param portfolioId the ID of the portfolio
     * @param symbol the symbol of the holding to remove
     * @return the updated portfolio
     * @throws ResourceNotFoundException if the portfolio is not found
     */
    public Portfolio removeHolding(Long portfolioId, String symbol) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id " + portfolioId));

        /*if (!portfolio.getHoldings().contains(symbol)) {
            throw new ExchangeRateException("The portfolio doesn't hold the cryptocurrency: " + symbol);
        }*/

        if (!portfolio.getHoldings().removeIf(h -> h.getSymbol().equals(symbol)))
        {
            throw new ExchangeRateException("The portfolio doesn't hold the cryptocurrency: " + symbol);
        }
        portfolioRepository.save(portfolio);
        return portfolio;
    }
    /**
     * Calculates the valuation of a portfolio in a specified base currency.
     *
     * @param portfolioId the ID of the portfolio
     * @param baseCurrency the base currency for valuation
     * @return the total valuation of the portfolio
     * @throws ResourceNotFoundException if the portfolio is not found
     */
    public double getPortfolioValuation(Long portfolioId, String baseCurrency) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id " + portfolioId));
        double totalValue = 0.0;
        for (Holding holding : portfolio.getHoldings()) {
            double price = exchangeRateService.getExchangeRate(holding.getSymbol(), baseCurrency, null);
           totalValue += holding.getAmount() * price;
        }
        return totalValue;
    }
    /**
     * Calculates the variation in valuation of a portfolio between two dates.
     *
     * @param portfolioId the ID of the portfolio
     * @param dateFrom the start date for the variation calculation
     * @param dateTo the end date for the variation calculation
     * @param baseCurrency the base currency for valuation
     * @return the percentage variation in valuation
     * @throws ResourceNotFoundException if the portfolio is not found
     */
    public double getPortfolioVariation(Long portfolioId, LocalDate dateFrom, LocalDate dateTo, String baseCurrency) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id " + portfolioId));
        double initialValuation = getHistoricalValuation(portfolio, dateFrom, baseCurrency);
        double finalValuation = getHistoricalValuation(portfolio, dateTo, baseCurrency);

        return (finalValuation - initialValuation) / initialValuation * 100;
    }

    /**
     * Calculates the historical valuation of a portfolio on a specific date.
     *
     * @param portfolio the portfolio
     * @param date the date for the historical valuation
     * @param baseCurrency the base currency for valuation
     * @return the total historical valuation of the portfolio
     */
    public double getHistoricalValuation(Portfolio portfolio, LocalDate date, String baseCurrency) {
        double totalValue = 0.0;
        for (Holding holding : portfolio.getHoldings()) {
            double price = exchangeRateService.getExchangeRate(holding.getSymbol(), baseCurrency, date);
            totalValue += holding.getAmount() * price;
        }
        return totalValue;
    }
}