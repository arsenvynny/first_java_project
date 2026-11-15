package service;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import service.impl.RepairServiceImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetCommercialOrderPriceTest {

    private Order order;
    private RepairServiceImpl repairOrderServiceImpl;

    @BeforeEach
    void setUp() {
        Client client = new Client("Arsen", "Vynnytskyi");
        Car car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");
        order = new Order(car, Priority.NORMAL);
        repairOrderServiceImpl = RepairOrderServiceTestSupport.fixtureFor(order).service();
    }

    @ParameterizedTest
    @CsvSource({
            "100, 200, 300, 400, NORMAL, 1300",
            "100, 200, 300, 400, URGED, 1404",
            "300, 500, 800, 1000, NORMAL, 3380",
            "20, 30, 0, 0, NORMAL, 130"
    })
    void getCommercialOrderPrice_appliesCommercialPricingRules_returnsExpectedPrice(
            String workCost1, String partsCost1, String workCost2, String partsCost2,
            Priority priority, String expected) {
        order.setPriority(priority);
        order.getWorks().add(new Work("Change wheels",
                workCost1, partsCost1, Specialization.GENERAL_SERVICE));
        order.getWorks().add(new Work("Fix accumulator",
                workCost2, partsCost2, Specialization.ELECTRICIAN));

        assertEquals(0, repairOrderServiceImpl.getCommercialOrderPrice(order)
                .compareTo(new BigDecimal(expected)));
    }
}
