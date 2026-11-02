package pricing;

import java.math.BigDecimal;

public class UrgentPriceRule implements PricingRule {
    private static final BigDecimal URGENT_FEE = new BigDecimal("0.2");
    @Override
    public BigDecimal apply(BigDecimal currentPrice, OrderPriceInfo info) {
        if (info.isUrgent()) {
            return currentPrice.add(info.workPrice().multiply(URGENT_FEE));
        }
        return currentPrice;
    }
}
