package runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import service.RepairService;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class ConsoleRunner implements CommandLineRunner {

    private final RepairService repairService;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1 - Create order");
            System.out.println("2 - Show order");
            System.out.println("3 - Exit");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> System.out.println("Order creation is not implemented yet.");
                case "2" -> System.out.println("Order lookup is not implemented yet.");
                case "3" -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }
    }
}
