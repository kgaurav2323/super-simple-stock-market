package com.demo.stockmarket.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kgaurav2323 on 9/10/23
 */
@Configuration
public class OpenApiConfig {

    /**
     * Open API configuration to enable REST endpoints
     * http://localhost:server.port/context.root/swagger-ui/index.html
     * http://localhost:server.port/context.root/v3/api-docs
     * @return
     */
    @Bean
    public OpenAPI openAPI(){
        Contact contact = new Contact();
        contact.setName("Kumar Gaurav");
        Info info = new Info()
                .title("Super Simple Stock Market API")
                .version("v1")
                .contact(contact)
                .description("This service exposes API endpoints to calculate " +
                        "1. Dividend yield, 2. P/E ratio, 3. Volume weighted stock price, 4. GBCE all share index and " +
                        "records Trade BUY/SELL transactions");
        return new OpenAPI().info(info);
    }
}
