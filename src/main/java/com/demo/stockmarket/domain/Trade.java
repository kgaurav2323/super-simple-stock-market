package com.demo.stockmarket.domain;

import com.demo.stockmarket.enums.trade.TradeIndicator;
import com.demo.stockmarket.factory.Stock;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author kgaurav2323 on 9/10/23
 */
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
public class Trade {
    private final UUID id;
    private Stock stock;
    private final TradeIndicator tradeIndicator;
    private final BigDecimal stockPrice;
    private final int sharesQuantity;
    private final LocalDateTime timestamp;
}
