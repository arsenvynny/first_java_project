package pricing;


import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PricePolicy {

    private List<PricingRule> rules;

    public PricePolicy(List<PricingRule> rules) {
        this.rules = rules;
    }

    public BigDecimal getTotalPrice(OrderPriceInfo info) {
        BigDecimal totalPrice = info.workPrice().add(info.partsPrice());
        PricingRule combinedRule = rules.stream()
                .reduce((first, second) ->
                        (price, orderInfo) -> second.apply(first.apply(price, orderInfo), orderInfo))
                .orElse((price, orderInfo) -> price);
        return combinedRule.apply(totalPrice, info);
    }
}