package domain.work;

import domain.Order;
import domain.Specialization;
import domain.Work;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkTest {
    @Test
    void noArgsConstructorInitializesFieldsToDefaults() {
        Work work = new Work();

        assertNull(work.getId());
        assertNull(work.getDescription());
        assertFalse(work.isCompleted());
        assertNull(work.getWorkCost());
        assertNull(work.getAutoPartsCost());
        assertNull(work.getSpecialization());
        assertNull(work.getOrder());
    }

    @Test
    void accessorsCoverEveryField() {
        Work work = new Work();
        Order order = new Order();
        BigDecimal workCost = new BigDecimal("45.00");
        BigDecimal partsCost = new BigDecimal("12.50");

        work.setId(10L);
        work.setDescription("Replace filter");
        work.setCompleted(true);
        work.setWorkCost(workCost);
        work.setAutoPartsCost(partsCost);
        work.setSpecialization(Specialization.GENERAL_SERVICE);
        work.setOrder(order);

        assertEquals(10L, work.getId());
        assertEquals("Replace filter", work.getDescription());
        assertTrue(work.isCompleted());
        assertSame(workCost, work.getWorkCost());
        assertSame(partsCost, work.getAutoPartsCost());
        assertEquals(Specialization.GENERAL_SERVICE, work.getSpecialization());
        assertSame(order, work.getOrder());
    }
}
