package repository;

import domain.Specialization;
import domain.Work;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.impl.InMemoryWorkRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryWorkRepositoryTest {

    private InMemoryWorkRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkRepository();
    }

    @Test
    void save_newWork_assignsIdAndMakesItFindable() {
        Work work = new Work("Repair", "100", "50", Specialization.GENERAL_SERVICE);

        assertSame(work, repository.save(work));
        assertNotNull(work.getId());
        assertSame(work, repository.findById(work.getId()).orElseThrow());
        assertEquals(1, repository.getCount());
    }

    @Test
    void save_unallocatedId_ExceptionThrows() {
        Work work = new Work("Repair", "100", "50", Specialization.GENERAL_SERVICE);
        work.setId(Long.MAX_VALUE);

        assertThrows(IllegalStateException.class, () -> repository.save(work));
    }
}
