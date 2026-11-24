package service.impl;

import domain.Car;
import domain.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CarRepository;
import repository.ClientRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @Mock private CarRepository cars;
    @Mock private ClientRepository clients;
    private CarServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CarServiceImpl(cars, clients);
    }

    @Test
    void createResolvesOwnerAndPersistsCar() {
        Client owner = client(7L);
        Car car = car(null, client(7L));
        when(clients.findById(7L)).thenReturn(Optional.of(owner));
        when(cars.save(car)).thenReturn(car);

        assertSame(car, service.create(car));
        assertSame(owner, car.getOwner());
        verify(cars).save(car);
    }

    @Test
    void createRejectsExistingIdAndUnknownOwner() {
        Car persisted = car(3L, client(7L));
        assertThrows(IllegalArgumentException.class, () -> service.create(persisted));

        when(clients.findById(7L)).thenReturn(Optional.empty());
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.create(car(null, client(7L))));
        assertEquals("Client with id 7 does not exist", error.getMessage());
        verify(cars, never()).save(any());
    }

    @Test
    void createRejectsEachInvalidCarField() {
        assertInvalid(car(null, client(1L)), c -> c.setModel(null));
        assertInvalid(car(null, client(1L)), c -> c.setModel("  "));
        assertInvalid(car(null, client(1L)), c -> c.setModel("m".repeat(51)));
        assertInvalid(car(null, client(1L)), c -> c.setMark(null));
        assertInvalid(car(null, client(1L)), c -> c.setMark(" "));
        assertInvalid(car(null, client(1L)), c -> c.setMark("m".repeat(51)));
        assertInvalid(car(null, client(1L)), c -> c.setOwner(null));
        assertInvalid(car(null, new Client()), c -> { });
        assertInvalid(car(null, client(1L)), c -> c.setVinCode(null));
        assertInvalid(car(null, client(1L)), c -> c.setVinCode(" ".repeat(17)));
        assertInvalid(car(null, client(1L)), c -> c.setVinCode("short"));
        assertInvalid(null, c -> { });
        verifyNoInteractions(cars, clients);
    }

    @Test
    void updateRequiresPersistedCarAndExistingOwner() {
        assertThrows(IllegalArgumentException.class, () -> service.update(car(null, client(7L))));
        Car unknown = car(10L, client(7L));
        when(cars.existsById(10L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.update(unknown));

        Car known = car(10L, client(7L));
        when(cars.existsById(10L)).thenReturn(true);
        when(clients.findById(7L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.update(known));
        verify(cars, never()).save(any());
    }

    @Test
    void updateResolvesOwnerAndSavesPersistedCar() {
        Car car = car(10L, client(7L));
        Client owner = client(7L);
        when(cars.existsById(10L)).thenReturn(true);
        when(clients.findById(7L)).thenReturn(Optional.of(owner));
        when(cars.save(car)).thenReturn(car);

        assertSame(car, service.update(car));
        assertSame(owner, car.getOwner());
        verify(cars).save(car);
    }

    @Test
    void readsAndDeletesCars() {
        Car car = car(1L, client(2L));
        when(cars.findById(1L)).thenReturn(Optional.of(car));
        when(cars.findAll()).thenReturn(List.of(car));

        assertSame(car, service.findById(1L));
        assertEquals(List.of(car), service.findAll());
        service.delete(1L);
        verify(cars).delete(car);

        when(cars.findById(9L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.findById(9L));
        assertThrows(IllegalArgumentException.class, () -> service.delete(9L));
    }

    private void assertInvalid(Car car, java.util.function.Consumer<Car> mutation) {
        if (car != null) mutation.accept(car);
        assertThrows(IllegalArgumentException.class, () -> service.create(car));
    }

    private static Car car(Long id, Client owner) {
        return new Car(id, "Civic", "Honda", 2020, owner, "12345678901234567");
    }

    private static Client client(Long id) {
        return new Client(id, "Ada", "Lovelace");
    }
}
