# Super-Simple-Stock-Market application
Refer below Implementation approach followed for Super Simple Stock Market Problem Statement

### Problem Statement

---

1. Provide working source code that will :-
   - a. For a given stock,
        - Given any price as input, calculate the dividend yield
        - Given any price as input, calculate the P/E Ratio
        - Record a trade, with timestamp, quantity of shares, buy or sell indicator and traded price
        - Calculate Volume Weighted Stock Price based on trades in past 15 minutes
        
    b. Calculate the GBCE All Share Index using the geometric mean of prices for all stocks
 

### Solution

---

##### Tech Stack

- Source: Java 8
- Spring boot : 2.7.16
- Open API : 3.0.1
- Build System: Maven - 3.8.1
- Test Framework: 
  - Integration test : Mock MVC framework
  - JUnit test       : Mockito/JUnit 5

##### Following Java design pattern used
1. Singleton
2. Factory
3. Builder
4. Facade

##### How to build and run test cases

- To build the source;
    ```
    mvn clean install
    ```
##### How to start stock-market application
- To run the project
    ```
    mvn spring-boot:run
    ```

##### API specification will be accessible by below URL after application startup
    - In HTML format : http://localhost:8080/stock-market/swagger-ui/index.html#/
    - In JSON format : http://localhost:8080/stock-market/v3/api-docs
