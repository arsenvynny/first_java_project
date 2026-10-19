package domain.RepairOrderTest;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import repository.InMemoryRepairOrderRepository;
import repository.RepairOrderRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class GetWorkCostTest {

    private Client client;
    private Car car;
    private RepairOrder order;

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

    }

    @ParameterizedTest
    @CsvSource({
            "100, 200, 300, 400, 400",
            "300, 500, 800, 1000, 1100"
    })
    void getWorkPrice_CalculateWorkCost_isCorrectPrice(String workCost1, String partsCost1,
                                                         String workCost2, String partsCost2, String expected){

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

        assertEquals(new BigDecimal(expected), order.getWorkPrice());
    }

}

