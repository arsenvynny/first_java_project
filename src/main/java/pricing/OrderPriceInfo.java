package pricing;

import java.math.BigDecimal;

public record OrderPriceInfo(BigDecimal workPrice, BigDecimal partsPrice, boolean isUrgent) {
}
