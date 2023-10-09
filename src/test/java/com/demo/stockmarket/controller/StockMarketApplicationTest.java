package com.demo.stockmarket.controller;

import com.demo.stockmarket.BaseIntegrationTest;
import com.demo.stockmarket.enums.stock.StockSymbol;
import com.demo.stockmarket.factory.Stock;
import com.demo.stockmarket.factory.StockFactory;
import com.demo.stockmarket.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author kgaurav2323 on 9/10/23
 */
@DirtiesContext
class StockMarketApplicationTest extends BaseIntegrationTest {
	@Autowired
	StockService stockService;
	private StockFactory stockFactory;
	private final String baseUrl = "/v1/stock";
	private final String dividendURL = "/dividend/{symbol}/{price}";
	private final String pByERatioURL = "/pe/ratio/{symbol}/{price}";
	private final String volumeWeightedStockPriceURL = "/volume/weighted/price/{symbol}/{interval}";
	private final String gbceAllShareIndexURL = "/gbce/share/index";

	@BeforeEach
	public void setup(){
		stockFactory = StockFactory.getInstance();

	}

	@Test
	public void dividendYield_commonStock_succeed() throws Exception {
		MockHttpServletResponse response = invokeGetRequestWithParam(baseUrl+dividendURL, StockSymbol.POP.name(), BigDecimal.valueOf(5));
		assertEquals("1.60", response.getContentAsString());
	}

	@Test
	public void dividendYield_prefStock_succeed() throws Exception {
		MockHttpServletResponse response = invokeGetRequestWithParam(baseUrl+dividendURL, StockSymbol.GIN.name(), BigDecimal.valueOf(5));
		assertEquals("40.00", response.getContentAsString());
	}

	@Test
	public void dividendYieldInvalidSymbol_failure() throws Exception {
		MockHttpServletResponse response = invokeGetRequestWithParam(baseUrl+dividendURL, "INVALID_STOCK_SYMBOL", BigDecimal.valueOf(5));
		assertEquals("Invalid stock symbol provided", response.getContentAsString());
	}

	@Test
	public void dividendYieldInvalidPrice_failure() throws Exception {
		MockHttpServletResponse response = invokeGetRequestWithParam(baseUrl+dividendURL, StockSymbol.GIN.name(), BigDecimal.ZERO);
		assertEquals("Stock price value - 0 can not be 0", response.getContentAsString());
	}

	@Test
	public void pByERatio_commonStock_succeed() throws Exception {
		MockHttpServletResponse response = invokeGetRequestWithParam(baseUrl+pByERatioURL, StockSymbol.JOE.name(), BigDecimal.valueOf(26));
		assertEquals("2.00", response.getContentAsString());
	}

	@Test
	public void pByERatio_prefStock_succeed() throws Exception {
		MockHttpServletResponse response = invokeGetRequestWithParam(baseUrl+pByERatioURL, StockSymbol.GIN.name(), BigDecimal.valueOf(15));
		assertEquals("1.88", response.getContentAsString());
	}

	@Test
	public void pByERatio_succeed_with_default_resp() throws Exception {
		MockHttpServletResponse response = invokeGetRequestWithParam(baseUrl+pByERatioURL, StockSymbol.TEA.name(), BigDecimal.valueOf(5));
		assertEquals("0", response.getContentAsString());
	}

	@Test
	public void volWeightedStockPrice_commonStock_succeed() throws Exception {
		Stock commonStock = stockFactory.getCommonStock(StockSymbol.POP, BigDecimal.TEN, BigDecimal.valueOf(100));
		generateCommonTradeData(commonStock);
		MockHttpServletResponse response = invokeGetRequestWithIntParam(baseUrl+volumeWeightedStockPriceURL, StockSymbol.POP.name(), 15);
		assertEquals("100.00", response.getContentAsString());
	}

	@Test
	public void volWeightedStockPrice_prefStock_succeed() throws Exception {
		Stock preferredStock = stockFactory.getPreferredStock(StockSymbol.GIN, BigDecimal.valueOf(8), BigDecimal.valueOf(100), BigDecimal.valueOf(2));
		generatePrefTradeData(preferredStock);
		MockHttpServletResponse response = invokeGetRequestWithIntParam(baseUrl+volumeWeightedStockPriceURL, StockSymbol.GIN.name(), 15);
		assertEquals("100.00", response.getContentAsString());
	}

	@Test
	public void volWeightedStockPrice_succeed_with_default_resp() throws Exception {
		Stock preferredStock = stockFactory.getPreferredStock(StockSymbol.GIN, BigDecimal.valueOf(8), BigDecimal.valueOf(100), BigDecimal.valueOf(2));
		generatePrefTradeData(preferredStock);
		MockHttpServletResponse response = invokeGetRequestWithIntParam(baseUrl+volumeWeightedStockPriceURL, "TEST", 15);
		assertEquals("0", response.getContentAsString());
	}

	@Test
	public void volWeightedStockPrice_failure() throws Exception {
		MockHttpServletResponse response = invokeGetRequestWithIntParam(baseUrl+volumeWeightedStockPriceURL, StockSymbol.GIN.name(),-15);
		assertEquals("Stock time interval can not be negative", response.getContentAsString());
	}

	@Test
	public void allShareIndexGeometricMean_should_succeed() throws Exception {
		Stock commonStock1 = stockFactory.getCommonStock(StockSymbol.TEA, BigDecimal.TEN, BigDecimal.valueOf(25));
		generateCommonTradeData(commonStock1);
		Stock commonStock2 = stockFactory.getCommonStock(StockSymbol.POP, BigDecimal.TEN, BigDecimal.valueOf(50));
		generateCommonTradeData(commonStock2);
		Stock commonStock3 = stockFactory.getCommonStock(StockSymbol.ALE, BigDecimal.TEN, BigDecimal.valueOf(100));
		generateCommonTradeData(commonStock3);
		Stock commonStock4 = stockFactory.getCommonStock(StockSymbol.JOE, BigDecimal.TEN, BigDecimal.valueOf(200));
		generateCommonTradeData(commonStock4);
		Stock preferredStock = stockFactory.getPreferredStock(StockSymbol.GIN, BigDecimal.valueOf(8), BigDecimal.valueOf(100), BigDecimal.valueOf(2));
		generatePrefTradeData(preferredStock);
		MockHttpServletResponse response = invokeGetRequest(baseUrl+gbceAllShareIndexURL);
		assertEquals("100.00", response.getContentAsString());
	}

	private void generateCommonTradeData(Stock stock) {
		// record trade bought in last 15 min
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now());
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(2));
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(1));
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(7));
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(5));
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(10));

		// record trade sold in last 15 min
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now());
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(3));
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(5));
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(8));
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(12));
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(10));

		// record trade bought outside 15 minutes window
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(55));
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(21));

		// record trade sold outside 15 minutes window
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(25));
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(40));
	}
	private void generatePrefTradeData(Stock stock) {
		// record trade bought in last 15 min
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now());
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(1));
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(3));
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(5));
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(10));
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(14));

		// record trade sold in last 15 min
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now());
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(2));
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(4));
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(5));
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(11));
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(13));

		// record trade bought outside 15 minutes window
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(35));
		stockService.buyStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(16));

		// record trade sold outside 15 minutes window
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(45));
		stockService.sellStock(stock, BigDecimal.valueOf(100), 10, LocalDateTime.now().minusMinutes(20));
	}





}
