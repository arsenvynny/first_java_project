package domain.mechanic;

import domain.Mechanic;
import domain.Specialization;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MechanicTest {
    @Test
    void noArgsConstructorSetsAvailabilityAndCreatesIndependentSpecializationLists() {
        Mechanic first = new Mechanic();
        Mechanic second = new Mechanic();

        assertNull(first.getId());
        assertNull(first.getName());
        assertTrue(first.isAvailable());
        assertNotNull(first.getSpecializationList());
        assertTrue(first.getSpecializationList().isEmpty());
        assertNotSame(first.getSpecializationList(), second.getSpecializationList());
    }

    @Test
    void accessorsCoverEveryField() {
        Mechanic mechanic = new Mechanic();
        List<Specialization> specializations = new ArrayList<>(List.of(
                Specialization.ENGINE_REPAIR, Specialization.ELECTRICIAN));

        mechanic.setId(8L);
        mechanic.setName("Morgan");
        mechanic.setAvailable(false);
        mechanic.setSpecializationList(specializations);

        assertEquals(8L, mechanic.getId());
        assertEquals("Morgan", mechanic.getName());
        assertFalse(mechanic.isAvailable());
        assertSame(specializations, mechanic.getSpecializationList());
    }
}
