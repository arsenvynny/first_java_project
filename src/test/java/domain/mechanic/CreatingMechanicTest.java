package domain.mechanic;

import domain.Mechanic;
import domain.Specialization;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CreatingMechanicTest {

    @Test
    void Mechanic_createMechanic_isCreated() {

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.CHASSIS);

        String name = "Arsen";
        Mechanic mechanic = new Mechanic(name, specializations);

        assertEquals(name, mechanic.getName());
        assertEquals(specializations, mechanic.getSpecializationList());
    }

    @Test
    void Mechanic_createMechanicWithNullName_ExceptionThrow() {

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.CHASSIS);

        String name = null;
        assertThrows(IllegalArgumentException.class, () -> new Mechanic(name, specializations));
    }

    @Test
    void Mechanic_createMechanicWithNullSpecializationListValue_ExceptionThrow() {

        List<Specialization> specializations = null;

        String name = "Arsen";
        assertThrows(IllegalArgumentException.class, () -> new Mechanic(name, specializations));
    }

    @Test
    void Mechanic_createMechanicWithEmptySpecializationListValue_ExceptionThrow() {

        List<Specialization> specializations = new ArrayList<>();

        String name = "Arsen";
        assertThrows(IllegalArgumentException.class, () -> new Mechanic(name, specializations));
    }

}
