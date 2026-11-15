package domain.work;

import domain.Specialization;
import domain.Work;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WorkTest {

    @Test
    void Work_constructor_setsWorkFields() {

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
    void Work_constructorWithInvalidDescription_ExceptionThrows() {

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
    void Work_constructorWithInvalidWorkCost_ExceptionThrows() {

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
    void Work_constructorWithInvalidPartsCost_ExceptionThrows() {

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
            (String workCost, String partsCost) {

        String description = "wheels change";
        Specialization specialization = Specialization.GENERAL_SERVICE;

        assertThrows(IllegalArgumentException.class,
                () -> new Work(description, workCost, partsCost, specialization));
    }
}
