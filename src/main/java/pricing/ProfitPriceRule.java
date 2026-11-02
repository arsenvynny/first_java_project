package pricing;

import java.math.BigDecimal;

public class ProfitPriceRule implements PricingRule {
    private static final BigDecimal PROFIT_MARGIN = new BigDecimal("0.30");

    @Override
    public BigDecimal apply(BigDecimal currentPrice, OrderPriceInfo info) {
        return currentPrice.add(currentPrice.multiply(PROFIT_MARGIN));
    }
}
