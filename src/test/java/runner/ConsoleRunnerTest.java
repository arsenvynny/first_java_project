package runner;

import domain.Order;
import domain.Priority;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import repository.RepairOrderRepository;
import service.OrderService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ConsoleRunnerTest {
    private final OrderService orderService = mock(OrderService.class);
    private final RepairOrderRepository orderRepository = mock(RepairOrderRepository.class);
    private final ConsoleRunner runner = new ConsoleRunner(orderService, orderRepository);
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    @AfterEach
    void restoreConsole() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void runHandlesPlaceholderActionsInvalidInputAndExit() {
        String output = runWithInput("1\n2\ninvalid\n3\n");

        assertTrue(output.contains("Order creation is not implemented yet."));
        assertTrue(output.contains("Order lookup is not implemented yet."));
        assertTrue(output.contains("Invalid option. Please choose 1, 2, 3, or 4."));
        verifyNoInteractions(orderService, orderRepository);
    }

    @Test
    void conflictDemoReportsOptimisticLockingFailureAndRestoresOriginalPriority() {
        Order stale = order(1L, Priority.NORMAL);
        Order concurrent = order(1L, Priority.NORMAL);
        Order updated = order(1L, Priority.URGED);
        when(orderService.findById(1L)).thenReturn(stale, concurrent);
        when(orderService.update(concurrent)).thenReturn(updated);
        when(orderService.update(updated)).thenReturn(updated);
        doThrow(new ObjectOptimisticLockingFailureException(Order.class, 1L))
                .when(orderRepository).save(stale);

        String output = runWithInput("4\n1\n3\n");

        assertEquals(Priority.NORMAL, updated.getPriority());
        assertTrue(output.contains("Optimistic locking conflict reproduced:"));
        verify(orderService).update(concurrent);
        verify(orderService).update(updated);
    }

    @Test
    void conflictDemoReportsNonNumericId() {
        String output = runWithInput("4\nnot-a-number\n3\n");

        assertTrue(output.contains("Order id must be a number."));
        verifyNoInteractions(orderService, orderRepository);
    }

    @Test
    void conflictDemoReportsWhenRepositoryDoesNotDetectConflict() {
        Order stale = order(2L, Priority.URGED);
        Order concurrent = order(2L, Priority.URGED);
        Order updated = order(2L, Priority.NORMAL);
        when(orderService.findById(2L)).thenReturn(stale, concurrent);
        when(orderService.update(concurrent)).thenReturn(updated);
        when(orderService.update(updated)).thenReturn(updated);
        when(orderRepository.save(stale)).thenReturn(stale);

        String output = runWithInput("4\n2\n3\n");

        assertEquals(Priority.URGED, updated.getPriority());
        assertTrue(output.contains("Could not reproduce an optimistic locking conflict."));
        verify(orderService).update(updated);
    }

    private String runWithInput(String input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        System.setOut(new PrintStream(output));
        runner.run();
        return output.toString(StandardCharsets.UTF_8);
    }

    private Order order(Long id, Priority priority) {
        Order order = new Order();
        order.setId(id);
        order.setPriority(priority);
        return order;
    }
}
