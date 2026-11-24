package app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.TimeZone;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

class CarServiceDemoTest {
    @Test
    void mainSetsUtcAndStartsSpringApplication() {
        assertNotNull(new CarServiceDemo());
        TimeZone original = TimeZone.getDefault();
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            CarServiceDemo.main(new String[0]);

            org.junit.jupiter.api.Assertions.assertEquals("UTC", TimeZone.getDefault().getID());
            springApplication.verify(() -> SpringApplication.run(CarServiceDemo.class, new String[0]));
        } finally {
            TimeZone.setDefault(original);
        }
    }
}
