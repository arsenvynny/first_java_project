package pricing;

import java.math.BigDecimal;

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
