package com.demo.stockmarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author kgaurav2323 on 9/10/23
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    protected MockHttpServletResponse invokeGetRequest(String url) throws Exception{
        return mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected MockHttpServletResponse invokeGetRequestWithParam(String url, String stockSymbol, BigDecimal price) throws Exception{
        return mockMvc.perform(get(url, stockSymbol, price)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();
    }
    protected MockHttpServletResponse invokeGetRequestWithIntParam(String url, String stockSymbol, Integer timeInterval) throws Exception{
        return mockMvc.perform(get(url, stockSymbol, timeInterval)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();
    }


}
