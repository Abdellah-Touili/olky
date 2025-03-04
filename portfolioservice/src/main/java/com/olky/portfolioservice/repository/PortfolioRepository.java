package com.olky.portfolioservice.repository;

import com.olky.portfolioservice.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}