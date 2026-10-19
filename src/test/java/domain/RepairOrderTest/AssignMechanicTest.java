package domain.RepairOrderTest;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryRepairOrderRepository;
import repository.RepairOrderRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssignMechanicTest {

    private Client client;
    private Car car;
    private RepairOrder order;
    private RepairOrderRepository repository;
    private List<Work> works = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        repository = new InMemoryRepairOrderRepository();

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        order = new RepairOrder(car, Priority.NORMAL);

        Work work1 = new Work("Change wheels",
                             "100",
                             "4000",
                             Specialization.GENERAL_SERVICE);

        Work work2 = new Work("fix accumulator",
                              "100",
                              "4000",
                              Specialization.ELECTRICIAN);

        order.diagnose();
        order.addWork(work1);
        order.addWork(work2);

    }

    @Test
    void assignMechanic_addingMechanic_MechanicIsAdded_MechanicIsBusy(){

        order.approve();

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.ELECTRICIAN);

        Mechanic mechanic = new Mechanic("Arsen", specializations);

        order.assignMechanic(mechanic);

        assertNotNull(order.getMechanic());
        assertFalse(mechanic.isAvailable());
    }

    @Test
    void assignMechanic_addingMechanicToNotApprovedOrder_ExceptionThrow(){

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.ELECTRICIAN);

        Mechanic mechanic = new Mechanic("Arsen", specializations);

        assertThrows(IllegalStateException.class, () -> order.assignMechanic(mechanic));
    }

    @Test
    void assignMechanic_addingNullMechanicValue_ExceptionThrow(){

        Mechanic mechanic = null;

        assertThrows(IllegalStateException.class, () -> order.assignMechanic(mechanic));
    }

    @Test
    void assignMechanic_addingBusyMechanic_ExceptionThrow(){

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.ELECTRICIAN);

        Mechanic mechanic = new Mechanic("Arsen", specializations);
        mechanic.makeBusy();

        assertThrows(IllegalStateException.class, () -> order.assignMechanic(mechanic));
    }

    @Test
    void assignMechanic_addingMechanicWithDifferentSpecialization_ExceptionThrow(){

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.CHASSIS);

        Mechanic mechanic = new Mechanic("Arsen", specializations);

        assertThrows(IllegalStateException.class, () -> order.assignMechanic(mechanic));
    }

}
