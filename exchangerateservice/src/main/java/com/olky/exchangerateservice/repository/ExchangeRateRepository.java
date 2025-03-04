package com.olky.exchangerateservice.repository;

import com.olky.exchangerateservice.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;
@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findBySymbolAndBaseAndDate(String symbol, String base, LocalDate date);
}
