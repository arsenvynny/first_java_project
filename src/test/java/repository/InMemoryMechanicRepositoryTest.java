package repository;

import domain.Mechanic;
import domain.Specialization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.impl.InMemoryMechanicRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryMechanicRepositoryTest {

    private InMemoryMechanicRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryMechanicRepository();
    }

    @Test
    void save_newMechanic_assignsIdAndMakesItFindable() {
        Mechanic mechanic = new Mechanic("Alex", List.of(Specialization.GENERAL_SERVICE));

        assertSame(mechanic, repository.save(mechanic));
        assertNotNull(mechanic.getId());
        assertSame(mechanic, repository.findById(mechanic.getId()).orElseThrow());
        assertEquals(1, repository.getCount());
    }

    @Test
    void save_unallocatedId_ExceptionThrows() {
        Mechanic mechanic = new Mechanic("Alex", List.of(Specialization.GENERAL_SERVICE));
        mechanic.setId(Long.MAX_VALUE);

        assertThrows(IllegalStateException.class, () -> repository.save(mechanic));
    }
}
