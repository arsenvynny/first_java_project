package pricing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class PricePolicyTest {

    @Test
    void noRulesReturnTheSumOfWorkAndPartsPrices() {
        PricePolicy policy = new PricePolicy(List.of());

        BigDecimal result = policy.getTotalPrice(info("120.00", "45.50", false));

        assertEquals(new BigDecimal("165.50"), result);
    }

    @Test
    void singleRuleReceivesThePriceInfoAndAppliesToTheCombinedPrice() {
        OrderPriceInfo orderInfo = info("120.00", "45.50", true);
        List<BigDecimal> inputs = new ArrayList<>();
        List<OrderPriceInfo> infos = new ArrayList<>();
        PricingRule rule = (price, receivedInfo) -> {
            inputs.add(price);
            infos.add(receivedInfo);
            return price.add(new BigDecimal("10.00"));
        };
        PricePolicy policy = new PricePolicy(List.of(rule));

        BigDecimal result = policy.getTotalPrice(orderInfo);

        assertEquals(new BigDecimal("175.50"), result);
        assertEquals(List.of(new BigDecimal("165.50")), inputs);
        assertSame(orderInfo, infos.getFirst());
    }

    @Test
    void multipleRulesAreComposedInListOrder() {
        List<String> calls = new ArrayList<>();
        OrderPriceInfo orderInfo = info("100.00", "20.00", true);
        PricingRule addWorkPrice = (price, receivedInfo) -> {
            calls.add("work-price");
            assertSame(orderInfo, receivedInfo);
            return price.add(receivedInfo.workPrice());
        };
        PricingRule doublePrice = (price, receivedInfo) -> {
            calls.add("double");
            assertSame(orderInfo, receivedInfo);
            return price.multiply(new BigDecimal("2"));
        };
        PricingRule subtractPartsPrice = (price, receivedInfo) -> {
            calls.add("parts-price");
            assertSame(orderInfo, receivedInfo);
            return price.subtract(receivedInfo.partsPrice());
        };
        PricePolicy policy = new PricePolicy(List.of(addWorkPrice, doublePrice, subtractPartsPrice));

        BigDecimal result = policy.getTotalPrice(orderInfo);

        assertEquals(new BigDecimal("420.00"), result);
        assertEquals(List.of("work-price", "double", "parts-price"), calls);
    }

    @ParameterizedTest
    @CsvSource({
            "0.00, 0.00, 130.00",
            "100.00, 0.00, 130.0000",
            "200.00, 50.00, 325.0000"
    })
    void policyRunsTheRealPricingRulesInTheirConfiguredOrder(String workPrice, String partsPrice, String expected) {
        PricePolicy policy = new PricePolicy(List.of(
                new MinOrderPriceRule(), new UrgentPriceRule(), new ProfitPriceRule()));

        BigDecimal result = policy.getTotalPrice(info(workPrice, partsPrice, false));

        assertEquals(0, new BigDecimal(expected).compareTo(result));
    }

    private static OrderPriceInfo info(String workPrice, String partsPrice, boolean urgent) {
        return new OrderPriceInfo(new BigDecimal(workPrice), new BigDecimal(partsPrice), urgent);
    }
}
