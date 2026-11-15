package service;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.RepairOrderRepository;
import service.impl.RepairServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;

public class AssignMechanicTest {

    private Client client;
    private Car car;
    private Order order;
    private RepairOrderRepository repository;
    private RepairServiceImpl repairOrderServiceImpl;
    private List<Work> works = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        order = new Order(car, Priority.NORMAL);
        RepairOrderServiceTestSupport.Fixture fixture = RepairOrderServiceTestSupport.fixtureFor(order);
        repository = fixture.repository();
        repairOrderServiceImpl = fixture.service();

        Work work1 = new Work("Change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        Work work2 = new Work("fix accumulator",
                "100",
                "4000",
                Specialization.ELECTRICIAN);

        repairOrderServiceImpl.diagnose(order);
        repairOrderServiceImpl.addWork(order, work1);
        repairOrderServiceImpl.addWork(order, work2);

    }

    @Test
    void assignMechanic_eligibleMechanic_assignsMechanicAndMarksBusy() {

        repairOrderServiceImpl.approve(order);

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.ELECTRICIAN);

        Mechanic mechanic = new Mechanic("Arsen", specializations);

        clearInvocations(repository);
        repairOrderServiceImpl.assignMechanic(order, mechanic);

        assertNotNull(order.getMechanic());
        assertFalse(mechanic.isAvailable());
        verify(repository).save(order);
    }

    @Test
    void assignMechanic_unapprovedOrder_ExceptionThrows() {

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.ELECTRICIAN);

        Mechanic mechanic = new Mechanic("Arsen", specializations);

        assertThrows(IllegalStateException.class, () -> repairOrderServiceImpl.assignMechanic(order, mechanic));
    }

    @Test
    void assignMechanic_nullMechanic_ExceptionThrows() {

        repairOrderServiceImpl.approve(order);

        Mechanic mechanic = null;

        assertThrows(IllegalStateException.class, () -> repairOrderServiceImpl.assignMechanic(order, mechanic));
    }

    @Test
    void assignMechanic_busyMechanic_ExceptionThrows() {

        repairOrderServiceImpl.approve(order);

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.ELECTRICIAN);

        Mechanic mechanic = new Mechanic("Arsen", specializations);
        mechanic.makeBusy();

        assertThrows(IllegalStateException.class, () -> repairOrderServiceImpl.assignMechanic(order, mechanic));
    }

    @Test
    void assignMechanic_incompatibleSpecialization_ExceptionThrows() {

        repairOrderServiceImpl.approve(order);

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.CHASSIS);

        Mechanic mechanic = new Mechanic("Arsen", specializations);

        assertThrows(IllegalStateException.class, () -> repairOrderServiceImpl.assignMechanic(order, mechanic));
    }

}
