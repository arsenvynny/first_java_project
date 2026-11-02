package domain.pricepolicy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pricing.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricePolicyTest {

    @ParameterizedTest
    @CsvSource({
            "600, 800, false, 1820.00",
            "600, 800, true, 1976.000"
    })
    void getTotalPrice_appliesConfiguredRules(String workPrice, String partsPrice,
                                              boolean urgent, String expected) {
        PricePolicy pricePolicy = new PricePolicy(List.of(
                new MinOrderPriceRule(),
                new UrgentPriceRule(),
                new ProfitPriceRule()));

        BigDecimal totalPrice = pricePolicy.getTotalPrice(new OrderPriceInfo(
                new BigDecimal(workPrice),
                new BigDecimal(partsPrice),
                urgent));

        assertEquals(0, totalPrice.compareTo(new BigDecimal(expected)));
    }
}
