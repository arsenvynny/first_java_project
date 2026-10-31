package domain.PricePolicyTest;

import domain.Car;
import domain.Client;
import domain.Order;
import domain.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pricing.PricePolicy;
import service.RepairOrderService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetTotalPartsTest {

    private Order order;
    private RepairOrderService repairOrderService;
    private Client client;
    private Car car;

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");
        repairOrderService = mock(RepairOrderService.class);
    }

    @ParameterizedTest
    @CsvSource({
            "200.0, 300.0, 400.0, 500.0, 1820.00",
            "100.0, 150.0, 200.0, 250.0, 910.00"
    })
    void getTotalPrice_CalculatingTotalNormalOrderPrice_isCorrect
            (String workCost1, String partsCost1, String workCost2, String partsCost2, String expected) {

        order = new Order(car, Priority.NORMAL);
        when(repairOrderService.getWorkPrice(order))
                .thenReturn(new BigDecimal(workCost1).add(new BigDecimal(workCost2)));
        when(repairOrderService.getPartsPrice(order))
                .thenReturn(new BigDecimal(partsCost1).add(new BigDecimal(partsCost2)));

        PricePolicy calculator = new PricePolicy(repairOrderService);

        assertEquals(expected, calculator.getTotalPrice(order).toString());
    }

    @ParameterizedTest
    @CsvSource({
            "200, 300, 400, 500, 1976.00",
            "100, 150, 200, 250, 988.00"
    })
    void getTotalPrice_CalculatingTotalUrgedOrderPrice_isCorrect
            (String workCost1, String partsCost1, String workCost2, String partsCost2, String expected) {

        order = new Order(car, Priority.URGED);
        when(repairOrderService.getWorkPrice(order))
                .thenReturn(new BigDecimal(workCost1).add(new BigDecimal(workCost2)));
        when(repairOrderService.getPartsPrice(order))
                .thenReturn(new BigDecimal(partsCost1).add(new BigDecimal(partsCost2)));

        PricePolicy calculator = new PricePolicy(repairOrderService);

        assertEquals(expected, calculator.getTotalPrice(order).toString());
    }
}
