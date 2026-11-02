package domain.pricepolicy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pricing.MinOrderPriceRule;
import pricing.OrderPriceInfo;
import pricing.ProfitPriceRule;
import pricing.UrgentPriceRule;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingRuleTest {

    @ParameterizedTest
    @CsvSource({
            "50, 100",
            "100, 100",
            "150, 150"
    })
    void minOrderPriceRule_appliesMinimum(String currentPrice, String expected) {
        BigDecimal result = new MinOrderPriceRule().apply(
                new BigDecimal(currentPrice),
                new OrderPriceInfo(new BigDecimal("0"), new BigDecimal("0"), false));

        assertEquals(0, result.compareTo(new BigDecimal(expected)));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "100, 130",
            "1200, 1560"
    })
    void profitPriceRule_addsThirtyPercent(String currentPrice, String expected) {
        BigDecimal result = new ProfitPriceRule().apply(
                new BigDecimal(currentPrice),
                new OrderPriceInfo(new BigDecimal("0"), new BigDecimal("0"), false));

        assertEquals(0, result.compareTo(new BigDecimal(expected)));
    }

    @ParameterizedTest
    @CsvSource({
            "100, 50, false, 100",
            "100, 50, true, 110",
            "250, 300, true, 310"
    })
    void urgentPriceRule_addsFeeOnlyForUrgentOrders(String currentPrice, String workPrice,
                                                     boolean urgent, String expected) {
        BigDecimal result = new UrgentPriceRule().apply(
                new BigDecimal(currentPrice),
                new OrderPriceInfo(new BigDecimal(workPrice), new BigDecimal("0"), urgent));

        assertEquals(0, result.compareTo(new BigDecimal(expected)));
    }


}
