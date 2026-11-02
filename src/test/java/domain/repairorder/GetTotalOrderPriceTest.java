package domain.repairorder;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetTotalOrderPriceTest {

    private Client client;
    private Car car;
    private Order order;
    private service.RepairOrderService repairOrderService;

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        order = new Order(car, Priority.NORMAL);
        repairOrderService = RepairOrderServiceTestSupport.fixtureFor(order).service();
    }

    @ParameterizedTest
    @CsvSource({
            "100, 200, 300, 400, 1300.00",
            "300, 500, 800, 1000, 3380.00"
    })
    void getWorkPrice_CalculateWorkCost_isCorrectPrice(String workCost1, String partsCost1,
                                                       String workCost2, String partsCost2, String expected) {

        Work work1 = new Work("Change wheels",
                workCost1,
                partsCost1,
                Specialization.GENERAL_SERVICE);

        Work work2 = new Work("fix accumulator",
                workCost2,
                partsCost2,
                Specialization.ELECTRICIAN);

        order.getWorks().add(work1);
        order.getWorks().add(work2);

        assertEquals(new BigDecimal(expected), repairOrderService.getTotalOrderPrice(order));
    }
}
