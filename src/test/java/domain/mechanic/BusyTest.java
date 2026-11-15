package domain.mechanic;

import domain.Mechanic;
import domain.Specialization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BusyTest {

    Mechanic mechanic;

    @BeforeEach
    void setUp() {

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.CHASSIS);

        String name = "Arsen";
        mechanic = new Mechanic(name, specializations);
    }

    @Test
    void makeBusy_mechanicState_changesToBusy() {

        mechanic.makeBusy();

        assertFalse(mechanic.isAvailable());
    }

    @Test
    void makeBusy_whenAlreadyBusy_ExceptionThrows() {

        mechanic.makeBusy();

        assertThrows(IllegalStateException.class, () -> mechanic.makeBusy());
    }

    @Test
    void makeFree_mechanicState_changesToFree() {

        mechanic.makeBusy();
        mechanic.makeFree();

        assertTrue(mechanic.isAvailable());
    }

    @Test
    void makeFree_whenAlreadyFree_remainsFree() {

        assertThrows(IllegalStateException.class, () -> mechanic.makeFree());

    }

}
