package pricing;

import domain.Priority;
import domain.RepairOrder;

import java.math.BigDecimal;

public class PricePolicy{
    static private final BigDecimal urgetFee = new BigDecimal("0.2");
    static private final BigDecimal profite = new BigDecimal("0.3");

    public BigDecimal getTotalPrice(final RepairOrder order){
        BigDecimal totalPrice = order.getTotalOrderPrice();

        if (order.getPriority() == Priority.URGED){
            totalPrice = totalPrice.add(totalPrice.multiply(urgetFee));
        }
        totalPrice = totalPrice.add(totalPrice.multiply(profite));

        return totalPrice;
    }
}