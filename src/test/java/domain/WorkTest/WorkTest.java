package domain.WorkTest;

import domain.Specialization;
import domain.Work;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import repository.InMemoryRepairOrderRepository;
import repository.RepairOrderRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkTest {

    @Test
    void Work_creatingWork_isCreated() {

        String description = "wheels change";
        String workCost = "100";
        String partsCost = "100";
        Specialization specialization = Specialization.GENERAL_SERVICE;

        Work work = new Work(description, workCost, partsCost, specialization);

        assertEquals(description, work.getDescription());
        assertEquals(workCost, work.getWorkCost().toString());
        assertEquals(partsCost, work.getAutoPartsCost().toString());
        assertEquals(specialization, work.getSpecialization());
    }

    @Test
    void Work_creatingWorkWithEmptyAndNullDescription_ExceptionThrow() {

        String description = null;
        String workCost = "100";
        String partsCost = "100";
        Specialization specialization = Specialization.GENERAL_SERVICE;

        assertThrows(IllegalArgumentException.class,
                () -> new Work(description, workCost, partsCost, specialization));

        String description2 = "";
        assertThrows(IllegalArgumentException.class,
                () -> new Work(description2, workCost, partsCost, specialization));
    }

    @Test
    void Work_creatingWorkWithEmptyAndNullWorkCost_ExceptionThrow() {

        String description = "wheels change";
        String workCost = null;
        String partsCost = "100";
        Specialization specialization = Specialization.GENERAL_SERVICE;

        assertThrows(IllegalArgumentException.class,
                () -> new Work(description, workCost, partsCost, specialization));

        String workCost2 = "";
        assertThrows(IllegalArgumentException.class,
                () -> new Work(description, workCost2, partsCost, specialization));
    }

    @Test
    void Work_creatingWorkWithEmptyAndNullPartsCost_ExceptionThrow() {

        String description = "wheels change";
        String workCost = "100";
        String partsCost = null;
        Specialization specialization = Specialization.GENERAL_SERVICE;

        assertThrows(IllegalArgumentException.class,
                () -> new Work(description, workCost, partsCost, specialization));

        String partsCost2 = "";
        assertThrows(IllegalArgumentException.class,
                () -> new Work(description, workCost, partsCost2, specialization));
    }

    @ParameterizedTest
    @CsvSource({
            "-13298, 100, 0, 200",
            "150, -0.1, 32, 0"
    })
    void Work_creatingWorkWithZeroOrMinusValue_ExceptionTrows
            (String workCost, String partsCost){

        String description = "wheels change";
        Specialization specialization = Specialization.GENERAL_SERVICE;

        assertThrows(IllegalArgumentException.class,
                () -> new Work(description, workCost, partsCost, specialization));
    }
}
