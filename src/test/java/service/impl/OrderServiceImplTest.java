package service.impl;

import domain.Car;
import domain.Mechanic;
import domain.Order;
import domain.OrderStatus;
import domain.Priority;
import domain.Work;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CarRepository;
import repository.RepairOrderRepository;
import repository.WorkRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock private RepairOrderRepository orders;
    @Mock private CarRepository cars;
    @Mock private WorkRepository works;
    private OrderServiceImpl service;

    @BeforeEach
    void setUp() { service = new OrderServiceImpl(orders, cars, works); }

    @Test
    void createResolvesCarInitializesStatusAndWorks() {
        Car car = car(3L);
        Order order = order(null, car(3L), Priority.URGED);
        order.setWorks(null);
        when(cars.findById(3L)).thenReturn(Optional.of(car));
        when(orders.save(order)).thenReturn(order);

        assertSame(order, service.create(order));
        assertSame(car, order.getCar());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertNotNull(order.getWorks());
        assertTrue(order.getWorks().isEmpty());
        verify(orders).save(order);
    }

    @Test
    void createAllowsAnEmptyWorkList() {
        Car car = car(3L);
        Order order = order(null, car(3L), Priority.NORMAL);
        when(cars.findById(3L)).thenReturn(Optional.of(car));
        when(orders.save(order)).thenReturn(order);

        assertSame(order, service.create(order));
        assertTrue(order.getWorks().isEmpty());
        verify(orders).save(order);
    }

    @Test
    void createRejectsPersistedOrdersMechanicsWorksAndMissingCar() {
        assertThrows(IllegalArgumentException.class,
                () -> service.create(order(1L, car(3L), Priority.NORMAL)));

        Order assigned = order(null, car(3L), Priority.NORMAL);
        assigned.setMechanic(new Mechanic());
        assertThrows(IllegalArgumentException.class, () -> service.create(assigned));

        Order withWorks = order(null, car(3L), Priority.NORMAL);
        withWorks.setWorks(List.of(new Work()));
        assertThrows(IllegalArgumentException.class, () -> service.create(withWorks));

        when(cars.findById(3L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.create(order(null, car(3L), Priority.NORMAL)));
        verify(orders, never()).save(any());
    }

    @Test
    void createAndUpdateValidateRequiredFields() {
        assertThrows(IllegalArgumentException.class, () -> service.create(null));
        assertInvalid(order(null, null, Priority.NORMAL));
        assertInvalid(order(null, new Car(), Priority.NORMAL));
        assertInvalid(order(null, car(3L), null));
        assertThrows(IllegalArgumentException.class, () -> service.update(null));
        assertThrows(IllegalArgumentException.class,
                () -> service.update(order(null, car(3L), Priority.NORMAL)));
        verifyNoInteractions(orders, cars, works);
    }

    @Test
    void updateChangesOnlyCarAndPriority() {
        Car oldCar = car(1L);
        Car newCar = car(2L);
        Order existing = order(9L, oldCar, Priority.NORMAL);
        existing.setStatus(OrderStatus.APPROVED);
        Mechanic assigned = new Mechanic();
        existing.setMechanic(assigned);
        Work existingWork = new Work();
        existing.setWorks(new ArrayList<>(List.of(existingWork)));
        Order changes = order(9L, car(2L), Priority.URGED);
        when(orders.findById(9L)).thenReturn(Optional.of(existing));
        when(cars.findById(2L)).thenReturn(Optional.of(newCar));
        when(orders.save(existing)).thenReturn(existing);

        assertSame(existing, service.update(changes));
        assertSame(newCar, existing.getCar());
        assertEquals(Priority.URGED, existing.getPriority());
        assertEquals(OrderStatus.APPROVED, existing.getStatus());
        assertSame(assigned, existing.getMechanic());
        assertEquals(List.of(existingWork), existing.getWorks());
        verify(orders).save(existing);
    }

    @Test
    void updateRejectsMissingOrderOrCar() {
        when(orders.findById(9L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.update(order(9L, car(2L), Priority.NORMAL)));

        Order existing = order(10L, car(1L), Priority.NORMAL);
        when(orders.findById(10L)).thenReturn(Optional.of(existing));
        when(cars.findById(2L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.update(order(10L, car(2L), Priority.NORMAL)));
        verify(orders, never()).save(any());
    }

    @Test
    void findAllAndDeleteDetachWorksBeforeDeletingOrder() {
        Order order = order(5L, car(1L), Priority.NORMAL);
        Work first = new Work();
        first.setOrder(order);
        Work second = new Work();
        second.setOrder(order);
        order.setWorks(new ArrayList<>(List.of(first, second)));
        when(orders.findById(5L)).thenReturn(Optional.of(order));
        when(orders.findAll()).thenReturn(List.of(order));

        assertSame(order, service.findById(5L));
        assertEquals(List.of(order), service.findAll());
        service.delete(5L);

        assertNull(first.getOrder());
        assertNull(second.getOrder());
        verify(works).saveAll(order.getWorks());
        verify(orders).delete(order);
    }

    @Test
    void deleteAlsoHandlesOrdersWithoutWorksAndMissingOrders() {
        Order order = order(7L, car(1L), Priority.NORMAL);
        order.setWorks(new ArrayList<>());
        when(orders.findById(7L)).thenReturn(Optional.of(order));
        service.delete(7L);
        verify(works).saveAll(order.getWorks());
        verify(orders).delete(order);

        when(orders.findById(8L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.findById(8L));
        assertThrows(IllegalArgumentException.class, () -> service.delete(8L));
    }

    private void assertInvalid(Order order) {
        assertThrows(IllegalArgumentException.class, () -> service.create(order));
    }

    private static Order order(Long id, Car car, Priority priority) {
        Order order = new Order();
        order.setId(id);
        order.setCar(car);
        order.setPriority(priority);
        return order;
    }

    private static Car car(Long id) {
        Car car = new Car();
        car.setId(id);
        return car;
    }
}
