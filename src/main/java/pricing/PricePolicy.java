package pricing;

import domain.Priority;
import domain.RepairOrder;

import java.math.BigDecimal;

public class PricePolicy{
    static private final BigDecimal URGENT_FEE = new BigDecimal("0.2");
    static private final BigDecimal PROFIT = new BigDecimal("0.3");

    public BigDecimal getTotalPrice(final RepairOrder order){
        BigDecimal totalPrice = order.getWorkPrice();

        if (order.getPriority() == Priority.URGED) {
            totalPrice = totalPrice.add(totalPrice.multiply(URGENT_FEE));
        }

        totalPrice = totalPrice.add(order.getPartsPrice());
        totalPrice = totalPrice.add(totalPrice.multiply(PROFIT));

        return totalPrice;
    }
}