package manager.impl;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pricing.OrderPriceInfo;
import pricing.PricePolicy;
import service.CarService;
import service.EventService;
import service.MechanicService;
import service.OrderService;
import service.WorkService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepairOrderManagerImplTest {
    private static final Long ORDER_ID = 10L;
    private static final Long WORK_ID = 20L;

    @Mock private PricePolicy pricePolicy;
    @Mock private CarService carService;
    @Mock private EventService eventService;
    @Mock private MechanicService mechanicService;
    @Mock private OrderService orderService;
    @Mock private WorkService workService;
    @InjectMocks private RepairOrderManagerImpl manager;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(ORDER_ID);
        order.setPriority(Priority.NORMAL);
        lenient().when(orderService.findById(ORDER_ID)).thenReturn(order);
        lenient().when(eventService.record(any(), any(), any())).thenAnswer(invocation -> {
            Event event = new Event();
            event.setOrder(invocation.getArgument(0));
            event.setType(invocation.getArgument(1));
            event.setDescription(invocation.getArgument(2));
            return event;
        });
    }

    @Test
    void createOrderLoadsCarCreatesOrderAndRecordsEvent() {
        Car car = new Car();
        car.setId(1L);
        when(carService.findById(1L)).thenReturn(car);
        when(orderService.create(any(Order.class))).thenAnswer(invocation -> {
            Order created = invocation.getArgument(0);
            created.setId(ORDER_ID);
            return created;
        });

        Order created = manager.createOrder(1L, Priority.URGED);

        assertSame(car, created.getCar());
        assertEquals(Priority.URGED, created.getPriority());
        verify(orderService).create(any(Order.class));
        verify(eventService).record(created, EventType.CREATED_ORDER, "Repair order created");
        assertEquals(1, created.getEvents().size());
    }

    @Test
    void createOrderRejectsMissingInputs() {
        assertThrows(IllegalArgumentException.class, () -> manager.createOrder(null, Priority.NORMAL));
        assertThrows(IllegalArgumentException.class, () -> manager.createOrder(1L, null));
        verifyNoInteractions(carService, orderService, eventService);
    }

    @Test
    void addWorksRejectsInvalidOrderStateAndInputs() {
        order.setStatus(OrderStatus.CREATED);
        assertThrows(IllegalStateException.class, () -> manager.addWorks(ORDER_ID, List.of(new Work())));

        order.setStatus(OrderStatus.DIAGNOSED);
        assertThrows(IllegalArgumentException.class, () -> manager.addWorks(ORDER_ID, null));
        assertThrows(IllegalArgumentException.class, () -> manager.addWorks(ORDER_ID, List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> manager.addWorks(ORDER_ID, java.util.Collections.singletonList(null)));
        verifyNoInteractions(workService);
    }

    @Test
    void addWorksCreatesAndAssociatesEachWorkAndRecordsEvents() {
        order.setStatus(OrderStatus.DIAGNOSED);
        Work first = new Work();
        Work second = new Work();
        when(workService.create(first)).thenAnswer(invocation -> {
            first.setId(21L);
            return first;
        });
        when(workService.create(second)).thenAnswer(invocation -> {
            second.setId(22L);
            return second;
        });

        manager.addWorks(ORDER_ID, List.of(first, second));

        assertEquals(List.of(first, second), order.getWorks());
        assertSame(order, first.getOrder());
        assertSame(order, second.getOrder());
        verify(eventService).record(order, EventType.WORK_ADDED, "Work 21 added to repair order");
        verify(eventService).record(order, EventType.WORK_ADDED, "Work 22 added to repair order");
    }

    @Test
    void diagnoseValidatesStateThenUpdatesAndRecordsEvent() {
        order.setStatus(OrderStatus.DIAGNOSED);
        assertThrows(IllegalStateException.class, () -> manager.diagnose(ORDER_ID));
        order.setStatus(OrderStatus.CREATED);

        manager.diagnose(ORDER_ID);

        assertEquals(OrderStatus.DIAGNOSED, order.getStatus());
        verify(eventService).record(order, EventType.DIAGNOSED_COMPLETED, "Diagnosis completed");
    }

    @Test
    void approveRequiresDiagnosisAndAtLeastOneWork() {
        assertThrows(IllegalStateException.class, () -> manager.approve(ORDER_ID));
        order.setStatus(OrderStatus.DIAGNOSED);
        assertThrows(IllegalStateException.class, () -> manager.approve(ORDER_ID));
        order.getWorks().add(new Work());

        manager.approve(ORDER_ID);

        assertEquals(OrderStatus.APPROVED, order.getStatus());
        verify(eventService).record(order, EventType.APPROVED_ORDER, "Repair order approved");
    }

    @Test
    void assignMechanicValidatesAllPreconditionsAndAssignsAvailableQualifiedMechanic() {
        assertThrows(IllegalStateException.class, () -> manager.assignMechanic(ORDER_ID, 4L));
        order.setStatus(OrderStatus.APPROVED);
        assertThrows(IllegalArgumentException.class, () -> manager.assignMechanic(ORDER_ID, null));
        order.setMechanic(new Mechanic());
        assertThrows(IllegalStateException.class, () -> manager.assignMechanic(ORDER_ID, 4L));
        order.setMechanic(null);

        Mechanic busy = new Mechanic();
        busy.setAvailable(false);
        when(mechanicService.findById(4L)).thenReturn(busy);
        assertThrows(IllegalStateException.class, () -> manager.assignMechanic(ORDER_ID, 4L));

        Mechanic wrongSpecialization = new Mechanic();
        wrongSpecialization.setAvailable(true);
        wrongSpecialization.setSpecializationList(List.of(Specialization.ENGINE_REPAIR));
        Work work = new Work();
        work.setSpecialization(Specialization.CHASSIS);
        order.getWorks().add(work);
        when(mechanicService.findById(5L)).thenReturn(wrongSpecialization);
        assertThrows(IllegalStateException.class, () -> manager.assignMechanic(ORDER_ID, 5L));

        Mechanic mechanic = new Mechanic();
        mechanic.setAvailable(true);
        mechanic.setSpecializationList(List.of(Specialization.CHASSIS));
        when(mechanicService.findById(6L)).thenReturn(mechanic);
        manager.assignMechanic(ORDER_ID, 6L);

        assertSame(mechanic, order.getMechanic());
        assertFalse(mechanic.isAvailable());
        verify(eventService).record(order, EventType.MECHANIC_ASSIGNED, "Mechanic assigned");
    }

    @Test
    void makeInProgressValidatesStateAndMechanic() {
        assertThrows(IllegalStateException.class, () -> manager.makeInProgress(ORDER_ID));
        order.setStatus(OrderStatus.APPROVED);
        assertThrows(IllegalStateException.class, () -> manager.makeInProgress(ORDER_ID));
        order.setMechanic(new Mechanic());

        manager.makeInProgress(ORDER_ID);

        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
        verify(eventService).record(order, EventType.START_WORKS, "Work started");
    }

    @Test
    void markWorkDoneValidatesAndCompletesWork() {
        assertThrows(IllegalArgumentException.class, () -> manager.markWorkDone(null));
        Work work = new Work();
        work.setId(WORK_ID);
        when(workService.findById(WORK_ID)).thenReturn(work);
        assertThrows(IllegalStateException.class, () -> manager.markWorkDone(WORK_ID));
        work.setOrder(order);
        order.setStatus(OrderStatus.DIAGNOSED);
        assertThrows(IllegalStateException.class, () -> manager.markWorkDone(WORK_ID));
        order.setStatus(OrderStatus.IN_PROGRESS);
        work.setCompleted(true);
        assertThrows(IllegalStateException.class, () -> manager.markWorkDone(WORK_ID));
        work.setCompleted(false);

        manager.markWorkDone(WORK_ID);

        assertTrue(work.isCompleted());
    }

    @Test
    void completeRequiresInProgressAndAllWorkDoneThenReleasesMechanic() {
        assertThrows(IllegalStateException.class, () -> manager.complete(ORDER_ID));
        order.setStatus(OrderStatus.IN_PROGRESS);
        Mechanic mechanic = new Mechanic();
        mechanic.setAvailable(false);
        order.setMechanic(mechanic);
        Work work = new Work();
        work.setCompleted(false);
        work.setWorkCost(BigDecimal.ZERO);
        work.setAutoPartsCost(BigDecimal.ZERO);
        order.getWorks().add(work);
        assertThrows(IllegalStateException.class, () -> manager.complete(ORDER_ID));

        work.setCompleted(true);
        when(pricePolicy.getTotalPrice(any(OrderPriceInfo.class))).thenReturn(new BigDecimal("125.00"));
        manager.complete(ORDER_ID);

        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        assertTrue(mechanic.isAvailable());
        assertEquals(new BigDecimal("125.00"), order.getFinalCost());
        assertNotNull(order.getCompletedAt());
        verify(eventService).record(order, EventType.COMPLETED_ALL_WORKS, "Repair order completed");
    }

    @Test
    void cancelRejectsTerminalOrders() {
        order.setStatus(OrderStatus.COMPLETED);
        assertThrows(IllegalStateException.class, () -> manager.cancel(ORDER_ID));
        order.setStatus(OrderStatus.CANCELLED);
        assertThrows(IllegalStateException.class, () -> manager.cancel(ORDER_ID));
    }

    @Test
    void cancelUnlinksUnfinishedWorksPricesCompletedWorksAndReleasesMechanic() {
        order.setStatus(OrderStatus.IN_PROGRESS);
        Mechanic mechanic = new Mechanic();
        mechanic.setAvailable(false);
        order.setMechanic(mechanic);
        Work done = new Work();
        done.setCompleted(true);
        done.setWorkCost(new BigDecimal("80.00"));
        done.setAutoPartsCost(new BigDecimal("20.00"));
        Work pending = new Work();
        pending.setId(30L);
        pending.setCompleted(false);
        order.getWorks().addAll(List.of(done, pending));
        when(pricePolicy.getTotalPrice(any(OrderPriceInfo.class))).thenReturn(new BigDecimal("120.00"));

        manager.cancel(ORDER_ID);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertTrue(mechanic.isAvailable());
        assertFalse(order.getWorks().contains(pending));
        assertNull(pending.getOrder());
        assertEquals(new BigDecimal("120.00"), order.getFinalCost());
        verify(workService).delete(30L);
        ArgumentCaptor<OrderPriceInfo> priceInfo = ArgumentCaptor.forClass(OrderPriceInfo.class);
        verify(pricePolicy).getTotalPrice(priceInfo.capture());
        assertEquals(new BigDecimal("80.00"), priceInfo.getValue().workPrice());
        assertEquals(new BigDecimal("20.00"), priceInfo.getValue().partsPrice());
    }

    @Test
    void cancelWithoutCompletedWorksSkipsRepricingAndHandlesUnassignedMechanic() {
        order.setStatus(OrderStatus.DIAGNOSED);

        manager.cancel(ORDER_ID);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verifyNoInteractions(pricePolicy);
        verify(eventService).record(order, EventType.CANCELLED_ORDER, "Repair order cancelled");
    }

    @Test
    void pricingMethodsAggregateWorkAndPartsAndApplyCommercialPolicy() {
        Work first = new Work();
        first.setWorkCost(new BigDecimal("30.00"));
        first.setAutoPartsCost(new BigDecimal("12.50"));
        Work second = new Work();
        second.setWorkCost(new BigDecimal("20.00"));
        second.setAutoPartsCost(new BigDecimal("7.50"));
        order.setPriority(Priority.URGED);
        order.getWorks().addAll(List.of(first, second));
        when(pricePolicy.getTotalPrice(any(OrderPriceInfo.class))).thenReturn(new BigDecimal("100.00"));

        assertEquals(new BigDecimal("70.00"), manager.getTotalOrderPrice(ORDER_ID));
        assertEquals(new BigDecimal("100.00"), manager.getCommercialOrderPrice(ORDER_ID));

        ArgumentCaptor<OrderPriceInfo> info = ArgumentCaptor.forClass(OrderPriceInfo.class);
        verify(pricePolicy).getTotalPrice(info.capture());
        assertEquals(new BigDecimal("50.00"), info.getValue().workPrice());
        assertEquals(new BigDecimal("20.00"), info.getValue().partsPrice());
        assertTrue(info.getValue().isUrgent());
    }

    @Test
    void pricingEmptyOrderReturnsZeroAndNonUrgentPolicyInput() {
        when(pricePolicy.getTotalPrice(any(OrderPriceInfo.class))).thenReturn(BigDecimal.ZERO);

        assertEquals(BigDecimal.ZERO, manager.getTotalOrderPrice(ORDER_ID));
        assertEquals(BigDecimal.ZERO, manager.getCommercialOrderPrice(ORDER_ID));

        verify(pricePolicy).getTotalPrice(new OrderPriceInfo(BigDecimal.ZERO, BigDecimal.ZERO, false));
    }

    @Test
    void pricingRequiresPersistedOrderId() {
        assertThrows(IllegalArgumentException.class, () -> manager.getTotalOrderPrice(null));
        assertThrows(IllegalArgumentException.class, () -> manager.getCommercialOrderPrice(null));
    }
}
