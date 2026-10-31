package domain.RepairOrderTest;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssignMechanicTest {

    private Client client;
    private Car car;
    private Order order;
    private service.RepairOrderService repairOrderService;
    private List<Work> works = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        repairOrderService = new service.RepairOrderService(
                org.mockito.Mockito.mock(repository.RepairOrderRepository.class));

        order = new Order(car, Priority.NORMAL);

        Work work1 = new Work("Change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        Work work2 = new Work("fix accumulator",
                "100",
                "4000",
                Specialization.ELECTRICIAN);

        repairOrderService.diagnose(order);
        repairOrderService.addWork(order, work1);
        repairOrderService.addWork(order, work2);

    }

    @Test
    void assignMechanic_addingMechanic_MechanicIsAdded_MechanicIsBusy() {

        repairOrderService.approve(order);

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.ELECTRICIAN);

        Mechanic mechanic = new Mechanic("Arsen", specializations);

        repairOrderService.assignMechanic(order, mechanic);

        assertNotNull(order.getMechanic());
        assertFalse(mechanic.isAvailable());
    }

    @Test
    void assignMechanic_addingMechanicToNotApprovedOrder_ExceptionThrow() {

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.ELECTRICIAN);

        Mechanic mechanic = new Mechanic("Arsen", specializations);

        assertThrows(IllegalStateException.class, () -> repairOrderService.assignMechanic(order, mechanic));
    }

    @Test
    void assignMechanic_addingNullMechanicValue_ExceptionThrow() {

        repairOrderService.approve(order);

        Mechanic mechanic = null;

        assertThrows(IllegalStateException.class, () -> repairOrderService.assignMechanic(order, mechanic));
    }

    @Test
    void assignMechanic_addingBusyMechanic_ExceptionThrow() {

        repairOrderService.approve(order);

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.ELECTRICIAN);

        Mechanic mechanic = new Mechanic("Arsen", specializations);
        mechanic.makeBusy();

        assertThrows(IllegalStateException.class, () -> repairOrderService.assignMechanic(order, mechanic));
    }

    @Test
    void assignMechanic_addingMechanicWithDifferentSpecialization_ExceptionThrow() {

        repairOrderService.approve(order);

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.CHASSIS);

        Mechanic mechanic = new Mechanic("Arsen", specializations);

        assertThrows(IllegalStateException.class, () -> repairOrderService.assignMechanic(order, mechanic));
    }

}
