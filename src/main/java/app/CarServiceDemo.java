package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"app", "config", "repository", "runner", "service"})
public class CarServiceDemo {
    public static void main(String[] args) {
        SpringApplication.run(CarServiceDemo.class, args);
    }
}
