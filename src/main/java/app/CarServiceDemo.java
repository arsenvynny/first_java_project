package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {
        "app", "config", "pricing", "repository", "runner", "service", "manager"
})
@EntityScan(basePackages = {"domain"})
@EnableJpaRepositories("repository")
public class CarServiceDemo {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(CarServiceDemo.class, args);
    }
}
