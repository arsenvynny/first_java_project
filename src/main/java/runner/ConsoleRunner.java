package runner;

import domain.Order;
import domain.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import repository.RepairOrderRepository;
import service.OrderService;

import java.util.Scanner;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ConsoleRunner implements CommandLineRunner {

    private final OrderService orderService;
    private final RepairOrderRepository orderRepository;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        Stream.generate(() -> {
                    System.out.println("1 - Create order");
                    System.out.println("2 - Show order");
                    System.out.println("3 - Exit");
                    System.out.println("4 - Demonstrate optimistic locking conflict");
                    return scanner.nextLine();
                })
                .takeWhile(input -> !"3".equals(input))
                .forEach(input -> {
            switch (input) {
                case "1" -> System.out.println("Order creation is not implemented yet.");
                case "2" -> System.out.println("Order lookup is not implemented yet.");
                case "4" -> demonstrateOptimisticLockingConflict(scanner);
                default -> System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
            }
        });
    }

    private void demonstrateOptimisticLockingConflict(Scanner scanner) {
        System.out.print("Order id: ");
        try {
            Long orderId = Long.parseLong(scanner.nextLine());
            Order staleOrder = orderService.findById(orderId);
            Order concurrentOrder = orderService.findById(orderId);
            Priority originalPriority = concurrentOrder.getPriority();
            concurrentOrder.setPriority(originalPriority == Priority.URGED ? Priority.NORMAL : Priority.URGED);
            Order updatedOrder = orderService.update(concurrentOrder);

            try {
                orderRepository.save(staleOrder);
            } catch (ObjectOptimisticLockingFailureException exception) {
                updatedOrder.setPriority(originalPriority);
                orderService.update(updatedOrder);
                throw exception;
            }

            updatedOrder.setPriority(originalPriority);
            orderService.update(updatedOrder);
            System.out.println("Could not reproduce an optimistic locking conflict.");
        } catch (ObjectOptimisticLockingFailureException exception) {
            System.out.println("Optimistic locking conflict reproduced: " + exception.getMessage());
        } catch (NumberFormatException exception) {
            System.out.println("Order id must be a number.");
        }
    }
}
