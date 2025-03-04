# Crypto Portfolio & Exchange Rate Microservices

Welcome to the **Crypto Portfolio & Exchange Rate Microservices** repository.

In this repository, you will find:

- A starter structure.
- The coding challenges requirements in this file

## Table of Contents
- [Overview](#overview)
- [Build Instructions](#build-instructions)
- [Run Instructions](#run-instructions)
- [Functional Requirements](#functional-requirements)
- [Technical Requirements](#technical-requirements)
- [How to Complete](#how-to-complete)


---

## Overview

This file outlines the **coding challenge** requirements and instructions. Your task is to implement two microservices : 
- **Exchange Rate Service**: Fetches or simulates crypto exchange rates (e.g., BTC, ETH in USD).  
- **Portfolio Service**: Manages user portfolios, including CRUD operations and valuation.


**Estimated Time**: Spend around 4–8 hours building a minimal yet functional solution that demonstrates:

- Spring Boot application structure & configuration
- Hibernate/JPA entity modeling & data persistence
- RESTful API design & best practices
- Coding standards & best practices
- Microservice architecture fundamentals


---

## Build Instructions

1. **Clone this repository** :
   ```bash
   git clone https://gitlab.com/olky_public/simple-portfolio.git
   cd crypto-portfolio

2. **Build the Exchange Rate Service** :
   ```bash
   cd exchangerateservice
   mvn clean install

3. **Build the Portfolio Service** :
   ```bash
   cd portfolioservice
   mvn clean install

---
## Run instructions
1. **Run the Exchange Rate Service**
   ```bash
   # Terminal 1:
   cd exchangerateservice
   mvn spring-boot:run

2. **Run the Portfolio Service** :
   ```bash
   # Terminal 2:
   cd portfolioservice
   mvn spring-boot:run

---

## Functional Requirements

### Exchange Rate Service
**Retrieve Crypto Prices**  
   - A REST endpoint that returns the current price for given crypto symbols (e.g., BTC, ETH) in a base currency (e.g., USD).  
   - You may **simulate** or **mock** an external API. (Bonus: integrate a real public crypto API like CoinGecko or CoinMarketCap.)


 **Endpoints**  
   - `GET /exchange-rate?symbol={symbol}&base={base}&date={date} – returns the current or historical price in the given base currency.

###  Portfolio Service

 **Manage Portfolios**  
   - A user can hold multiple crypto assets in a Portfolio.  
   - CRUD operations: create a new portfolio, add crypto holdings, remove holdings, list portfolios.

**Valuation**  
   - An endpoint that returns the total value of a portfolio in a given base currency (e.g. USD).  
   - Must call the Exchange Rate Service to get the latest price.

**Endpoints**  
   - `POST /portfolios` – create a new portfolio.  
   - `GET /portfolios/{id}` – get portfolio details (including each crypto holding).  
   - `POST /portfolios/{id}/holdings` – add a holding.  
   - `DELETE /portfolios/{id}/holdings/{symbol}` – remove a holding.  
   - `GET /portfolios/{id}/valuation?base={base}` – returns total value and crypto holding values in the given base currency.
   - `GET /portfolios/{id}/variation?dateFrom={dateFrom}&dateTo={dateTo}&base={base}` – returns the total change in the value of the portfolio for the given base currency, expressed in the currency and as a percentage between the parameter dates.

---
## Technical Requirements

- **Backend**: Java 17 and Spring Boot 3.4.1
- **Build Tool**: Maven.  
- **Data**:  Any relational database (H2 in-memory for simplicity).  
- **Persistence**: Hibernate/JPA for data access.  
- **API Communication**: Microservices communicate via REST calls (JSON).  
- **Tests**:  Provide **unit tests** (JUnit, Mockito) to validate services & controllers.  
- **Documentation**:   Endpoint documentation (Bonus: integrate a OpenAPI documentation).


### How to Complete
1. **Fork** this repository into a private gitlab repository
2. **Implement your solution**
3. **Test your solution**
4. **Document your solution**
5. **invite** @olky_public with the role Reporter so we can review your solution
