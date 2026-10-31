package pricing;

import domain.Order;
import domain.Priority;
import service.RepairOrderService;

import java.math.BigDecimal;

public class PricePolicy {
    static private final BigDecimal URGENT_FEE = new BigDecimal("0.2");
    static private final BigDecimal PROFIT = new BigDecimal("0.3");
    static private final BigDecimal DISCOUNT = new BigDecimal("0.1");

    public BigDecimal getTotalPrice(BigDecimal workPrice, BigDecimal partsPrice, Priority priority) {
        BigDecimal totalPrice = workPrice;
        if (priority == Priority.URGED) {
            totalPrice = totalPrice.add(totalPrice.multiply(URGENT_FEE));
        }

        totalPrice = totalPrice.add(partsPrice);
        totalPrice = totalPrice.add(totalPrice.multiply(PROFIT));

        return totalPrice;
    }

    public BigDecimal getTotalDiscountPrice(BigDecimal workPrice, BigDecimal partsPrice, Priority priority) {
        BigDecimal totalPrice = getTotalPrice(workPrice, partsPrice, priority);
        return totalPrice.subtract(totalPrice.multiply(DISCOUNT));
    }
}