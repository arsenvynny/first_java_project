package pricing;

import domain.Order;
import domain.Priority;
import service.RepairOrderService;

import java.math.BigDecimal;

public class PricePolicy {
    static private final BigDecimal URGENT_FEE = new BigDecimal("0.2");
    static private final BigDecimal PROFIT = new BigDecimal("0.3");
    static private final BigDecimal DISCOUNT = new BigDecimal("0.1");

    private final RepairOrderService repairOrderService;

    public PricePolicy(RepairOrderService repairOrderService) {
        this.repairOrderService = repairOrderService;
    }

    public BigDecimal getTotalPrice(final Order order) {
        BigDecimal totalPrice = repairOrderService.getWorkPrice(order);
        if (order.getPriority() == Priority.URGED) {
            totalPrice = totalPrice.add(totalPrice.multiply(URGENT_FEE));
        }

        totalPrice = totalPrice.add(repairOrderService.getPartsPrice(order));
        totalPrice = totalPrice.add(totalPrice.multiply(PROFIT));

        return totalPrice;
    }

    public BigDecimal getTotalDiscountPrice(final Order order) {
        BigDecimal totalPrice = repairOrderService.getWorkPrice(order);

        if (order.getPriority() == Priority.URGED) {
            totalPrice = totalPrice.add(totalPrice.multiply(URGENT_FEE));
        }

        totalPrice = totalPrice.add(repairOrderService.getPartsPrice(order));
        totalPrice = totalPrice.add(totalPrice.multiply(PROFIT));

        return totalPrice.add(totalPrice.multiply(DISCOUNT));
    }
}