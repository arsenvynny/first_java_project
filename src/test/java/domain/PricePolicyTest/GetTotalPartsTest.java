package domain.PricePolicyTest;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pricing.PricePolicy;
import repository.InMemoryRepairOrderRepository;
import repository.RepairOrderRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetTotalPartsTest {

    private RepairOrder order;
    private Client client;
    private Car car;

    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

    }
    @ParameterizedTest
    @CsvSource ({
            "200.0, 300.0, 400.0, 500.0, 1820.00",
            "100.0, 150.0, 200.0, 250.0, 910.00"
    })
    void getTotalPrice_CalculatingTotalNormalOrderPrice_isCorrect
            (String workCost1, String partsCost1, String workCost2, String partsCost2, String expected){

        order = new RepairOrder(car, Priority.NORMAL);

        Work work1 = new Work("Change wheels",
                workCost1,
                partsCost1,
                Specialization.GENERAL_SERVICE);

        Work work2 = new Work("fix accumulator",
                workCost2,
                partsCost2,
                Specialization.ELECTRICIAN);

        order.diagnose();
        order.addWork(work1);
        order.addWork(work2);

        PricePolicy calculator = new PricePolicy();

        assertEquals(expected, calculator.getTotalPrice(order).toString());
    }

    @ParameterizedTest
    @CsvSource ({
            "200, 300, 400, 500, 1976.00",
            "100, 150, 200, 250, 988.00"
    })
    void getTotalPrice_CalculatingTotalUrgedOrderPrice_isCorrect
            (String workCost1, String partsCost1, String workCost2, String partsCost2, String expected){

        order = new RepairOrder(car, Priority.URGED);

        Work work1 = new Work("Change wheels",
                workCost1,
                partsCost1,
                Specialization.GENERAL_SERVICE);

        Work work2 = new Work("fix accumulator",
                workCost2,
                partsCost2,
                Specialization.ELECTRICIAN);

        order.diagnose();
        order.addWork(work1);
        order.addWork(work2);

        PricePolicy calculator = new PricePolicy();

        assertEquals(expected, calculator.getTotalPrice(order).toString());
    }
}
