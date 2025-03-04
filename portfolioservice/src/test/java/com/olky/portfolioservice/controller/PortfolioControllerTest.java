package com.olky.portfolioservice.controller;

import com.olky.portfolioservice.model.Portfolio;
import com.olky.portfolioservice.service.PortfolioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PortfolioControllerTest {
    @Mock
    private PortfolioService portfolioService;
    @InjectMocks
    private PortfolioController portfolioController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc=MockMvcBuilders.standaloneSetup(portfolioController).build();
    }
    @Test
    public void testCreatePortfolio() throws Exception {
        Portfolio portfolio = new Portfolio();
        portfolio.setId(1L);
        portfolio.setName("Test Portfolio");

        when(portfolioService.createPortfolio(any(Portfolio.class))).thenReturn(portfolio);

        mockMvc.perform(post("/portfolios/createPortfolio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Portfolio\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Portfolio"));
    }
    @Test
    public void testGetPortfolioValuation() throws Exception {
        when(portfolioService.getPortfolioValuation(2L, "USD")).thenReturn(50000.0);

        mockMvc.perform(get("/portfolios/2/valuation/USD")
                        .param("base", "USD"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("50000.0"));
    }
    @Test
    public void testGetPortfolioVariation() throws Exception {
        LocalDate dateFrom = LocalDate.parse("2024-01-01");
        LocalDate dateTo = LocalDate.parse("2024-12-31");

        when(portfolioService.getPortfolioVariation(2L, dateFrom, dateTo,"USD")).thenReturn(50.0);

        mockMvc.perform(get("/portfolios/2/variation")
                        .param("dateFrom", "2024-01-01")
                        .param("dateTo", "2024-12-31")
                        .param("base", "USD"))
                .andExpect(status().isOk())
                .andExpect(content().string("50.0"));
    }
}