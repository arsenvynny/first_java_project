package pricing;

import domain.Order;
import domain.Priority;
import service.RepairOrderService;

import java.math.BigDecimal;
import java.util.List;

public class PricePolicy {

    private List<PricingRule> rules;

    public PricePolicy(List<PricingRule> rules) {
        this.rules = rules;
    }

    public BigDecimal getTotalPrice(OrderPriceInfo info) {
        BigDecimal totalPrice = info.workPrice().add(info.partsPrice());

        for (PricingRule rule : rules) {
            totalPrice = rule.apply(totalPrice, info);
        }

        return totalPrice;
    }
}