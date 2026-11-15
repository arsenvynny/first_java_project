package service;

import domain.Mechanic;
import domain.Specialization;
import org.junit.jupiter.api.Test;
import service.impl.MechanicServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class MechanicServiceValidationTest {

    @Test
    void createMechanic_validInput_setsMechanicFields() {
        List<Specialization> specializations = List.of(
                Specialization.GENERAL_SERVICE,
                Specialization.CHASSIS);
        Mechanic mechanic = new MechanicServiceImpl().createMechanic("Arsen", specializations);

        assertEquals("Arsen", mechanic.getName());
        assertEquals(specializations, mechanic.getSpecializationList());
    }

    @Test
    void createMechanic_nullName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new MechanicServiceImpl().createMechanic(null, List.of(Specialization.CHASSIS)));
    }

    @Test
    void createMechanic_nullSpecializations_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new MechanicServiceImpl().createMechanic("Arsen", null));
    }

    @Test
    void createMechanic_emptySpecializations_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new MechanicServiceImpl().createMechanic("Arsen", new ArrayList<>()));
    }

}
