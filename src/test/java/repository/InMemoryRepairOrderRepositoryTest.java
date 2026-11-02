package repository;

import domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryRepairOrderRepositoryTest {

    private InMemoryRepairOrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRepairOrderRepository();
    }

    @Test
    void save_newOrder_assignsIdAndStoresOrder() {
        Order order = new Order();

        Order savedOrder = repository.save(order);

        assertSame(order, savedOrder);
        assertNotNull(order.getId());
        assertSame(order, repository.findById(order.getId()).orElseThrow());
    }

    @Test
    void save_orderWithExistingId_replacesStoredOrder() {
        Order originalOrder = repository.save(new Order());
        Order updatedOrder = new Order();
        updatedOrder.setId(originalOrder.getId());

        repository.save(updatedOrder);

        assertSame(updatedOrder, repository.findById(originalOrder.getId()).orElseThrow());
        assertEquals(1, repository.getCount());
    }

    @Test
    void save_orderWithUnallocatedId_throwsException() {
        Order order = new Order();
        order.setId(Long.MAX_VALUE);

        assertThrows(IllegalStateException.class, () -> repository.save(order));
    }

    @Test
    void save_multipleNewOrders_assignsUniqueIds() {
        Order firstOrder = repository.save(new Order());
        Order secondOrder = repository.save(new Order());

        assertNotEquals(firstOrder.getId(), secondOrder.getId());
    }

    @Test
    void findById_unknownId_returnsEmpty() {
        assertEquals(java.util.Optional.empty(), repository.findById(Long.MAX_VALUE));
    }

    @Test
    void getCount_returnsNumberOfStoredOrders() {
        repository.save(new Order());
        repository.save(new Order());

        assertEquals(2, repository.getCount());
    }
}
