package domain.RepairOrderTest;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetPartsPriceTest {

    private Client client;
    private Car car;
    private Order order;
    private service.RepairOrderService repairOrderService;

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        repairOrderService = new service.RepairOrderService(
                org.mockito.Mockito.mock(repository.RepairOrderRepository.class));

        order = new Order(car, Priority.NORMAL);

    }

    @ParameterizedTest
    @CsvSource({
            "100, 200, 300, 400, 600",
            "300, 500, 800, 1000, 1500"
    })
    void getPartsPrice_CalculatePartsCost_isCorrectPrice(String workCost1, String partsCost1,
                                                         String workCost2, String partsCost2, String expected) {

        Work work1 = new Work("Change wheels",
                workCost1,
                partsCost1,
                Specialization.GENERAL_SERVICE);

        Work work2 = new Work("fix accumulator",
                workCost2,
                partsCost2,
                Specialization.ELECTRICIAN);

        repairOrderService.diagnose(order);
        repairOrderService.addWork(order, work1);
        repairOrderService.addWork(order, work2);

        assertEquals(new BigDecimal(expected), repairOrderService.getPartsPrice(order));
    }
}
