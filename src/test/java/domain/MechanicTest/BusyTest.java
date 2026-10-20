package domain.MechanicTest;

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
    void setUp(){

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.CHASSIS);

        String name = "Arsen";
        mechanic = new Mechanic(name, specializations);
    }

    @Test
    void makeBusy_TryToMakeMechanicBusy_isBusy(){

        mechanic.makeBusy();

        assertFalse(mechanic.isAvailable());
    }

    @Test
    void makeBusy_TryToMakeBusyAlreadyBusyMechanic_ExceptionThrow(){

        mechanic.makeBusy();

        assertThrows(IllegalStateException.class, () -> mechanic.makeBusy());
    }

    @Test
    void makeFree_TryToMakeMechanicFree_isBusy(){

        mechanic.makeBusy();
        mechanic.makeFree();

        assertTrue(mechanic.isAvailable());
    }

    @Test
    void makeFree_TryToMakeFreeAlreadyFreeMechanic_isBusy(){

        assertThrows(IllegalStateException.class, () -> mechanic.makeFree());

    }

}
