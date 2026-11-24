package pricing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PricingRuleTest {

    private final MinOrderPriceRule minimumRule = new MinOrderPriceRule();
    private final UrgentPriceRule urgentRule = new UrgentPriceRule();
    private final ProfitPriceRule profitRule = new ProfitPriceRule();

    @ParameterizedTest
    @CsvSource({
            "0, 100.00",
            "99.99, 100.00",
            "100.00, 100.00",
            "100.01, 100.01",
            "250.50, 250.50"
    })
    void minimumRuleRaisesPricesBelowMinimumAndPreservesPricesAtOrAboveIt(String input, String expected) {
        BigDecimal result = minimumRule.apply(new BigDecimal(input), info(false));

        assertEquals(0, new BigDecimal(expected).compareTo(result));
    }

    @ParameterizedTest
    @CsvSource({
            "true, 200.00, 30.00, 240.00",
            "false, 200.00, 30.00, 200.00",
            "true, 0.00, 30.00, 200.00"
    })
    void urgentRuleAddsTwentyPercentOfWorkPriceOnlyForUrgentOrders(
            boolean urgent, String workPrice, String partsPrice, String expected) {
        OrderPriceInfo info = new OrderPriceInfo(
                new BigDecimal(workPrice), new BigDecimal(partsPrice), urgent);

        BigDecimal result = urgentRule.apply(new BigDecimal("200.00"), info);

        assertEquals(0, new BigDecimal(expected).compareTo(result));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "100, 130.00",
            "125.50, 163.1500",
            "-10, -13.000"
    })
    void profitRuleAddsThirtyPercentOfTheCurrentPrice(String input, String expected) {
        BigDecimal result = profitRule.apply(new BigDecimal(input), info(false));

        assertEquals(0, new BigDecimal(expected).compareTo(result));
    }

    @Test
    void orderPriceInfoRecordProvidesValueSemantics() {
        OrderPriceInfo info = info(true);
        OrderPriceInfo equalInfo = new OrderPriceInfo(new BigDecimal("75.00"), new BigDecimal("24.50"), true);

        assertEquals(new BigDecimal("75.00"), info.workPrice());
        assertEquals(new BigDecimal("24.50"), info.partsPrice());
        assertTrue(info.isUrgent());
        assertEquals(info, equalInfo);
        assertEquals(info.hashCode(), equalInfo.hashCode());
        assertEquals("OrderPriceInfo[workPrice=75.00, partsPrice=24.50, isUrgent=true]", info.toString());
    }

    private static OrderPriceInfo info(boolean urgent) {
        return new OrderPriceInfo(new BigDecimal("75.00"), new BigDecimal("24.50"), urgent);
    }
}
