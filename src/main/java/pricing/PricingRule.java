package pricing;

import java.math.BigDecimal;

public interface PricingRule {
    BigDecimal apply(BigDecimal currentPrice, OrderPriceInfo info);
}
