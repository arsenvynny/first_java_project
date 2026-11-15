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
    void Mechanic_constructor_setsMechanicFields() {

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.CHASSIS);

        String name = "Arsen";
        Mechanic mechanic = new Mechanic(name, specializations);

        assertEquals(name, mechanic.getName());
        assertEquals(specializations, mechanic.getSpecializationList());
    }

    @Test
    void Mechanic_constructorWithNullName_ExceptionThrows() {

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.CHASSIS);

        String name = null;
        assertThrows(IllegalArgumentException.class, () -> new Mechanic(name, specializations));
    }

    @Test
    void Mechanic_constructorWithNullSpecialization_ExceptionThrows() {

        List<Specialization> specializations = null;

        String name = "Arsen";
        assertThrows(IllegalArgumentException.class, () -> new Mechanic(name, specializations));
    }

    @Test
    void Mechanic_constructorWithEmptySpecializations_ExceptionThrows() {

        List<Specialization> specializations = new ArrayList<>();

        String name = "Arsen";
        assertThrows(IllegalArgumentException.class, () -> new Mechanic(name, specializations));
    }

}
