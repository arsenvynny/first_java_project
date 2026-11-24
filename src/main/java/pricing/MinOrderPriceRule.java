package pricing;

import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;

@Component
@Order(1)
public class MinOrderPriceRule implements PricingRule {
    private static final BigDecimal MIN_ORDER_PRICE = new BigDecimal("100.00");
    @Override
    public BigDecimal apply(BigDecimal currentPrice, OrderPriceInfo info) {
        if (currentPrice.compareTo(MIN_ORDER_PRICE) < 0) {
            return MIN_ORDER_PRICE;
        }
        return currentPrice;
    }
}
