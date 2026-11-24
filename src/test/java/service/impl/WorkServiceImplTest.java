package service.impl;

import domain.Order;
import domain.Specialization;
import domain.Work;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.WorkRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkServiceImplTest {
    @Mock private WorkRepository repository;
    private WorkServiceImpl service;

    @BeforeEach
    void setUp() { service = new WorkServiceImpl(repository); }

    @Test
    void createResetsCompletionAndOrderAndSavesWork() {
        Work work = work(null, "Replace belt", "100.00", "25.50");
        work.setCompleted(true);
        work.setOrder(new Order());
        when(repository.save(work)).thenReturn(work);

        assertSame(work, service.create(work));
        assertFalse(work.isCompleted());
        assertNull(work.getOrder());
        verify(repository).save(work);
    }

    @Test
    void createRejectsPersistedOrInvalidWork() {
        assertThrows(IllegalArgumentException.class,
                () -> service.create(work(1L, "Valid", "1.00", "2.00")));
        assertInvalid(null);
        assertInvalid(work(null, null, "1.00", "2.00"));
        assertInvalid(work(null, " ", "1.00", "2.00"));
        assertInvalid(work(null, "x".repeat(256), "1.00", "2.00"));
        assertInvalid(work(null, "Valid", null, "2.00"));
        assertInvalid(work(null, "Valid", "-0.01", "2.00"));
        assertInvalid(work(null, "Valid", "10000000000.00", "2.00"));
        assertInvalid(work(null, "Valid", "1.000", "2.00"));
        assertInvalid(work(null, "Valid", "1.00", null));
        assertInvalid(work(null, "Valid", "1.00", "-0.01"));
        assertInvalid(work(null, "Valid", "1.00", "10000000000.00"));
        assertInvalid(work(null, "Valid", "1.00", "1.000"));
        Work noSpecialization = work(null, "Valid", "1.00", "2.00");
        noSpecialization.setSpecialization(null);
        assertInvalid(noSpecialization);
        assertThrows(IllegalArgumentException.class, () -> service.create(null));
        verifyNoInteractions(repository);
    }

    @Test
    void updateCopiesEditableValuesAndPreservesCompletionAndOrder() {
        Work existing = work(4L, "Old", "10.00", "5.00");
        existing.setCompleted(true);
        Order order = new Order();
        existing.setOrder(order);
        Work changes = work(4L, "New", "20.25", "7.75");
        changes.setSpecialization(Specialization.ELECTRICIAN);
        when(repository.findById(4L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        assertSame(existing, service.update(changes));
        assertEquals("New", existing.getDescription());
        assertEquals(new BigDecimal("20.25"), existing.getWorkCost());
        assertEquals(new BigDecimal("7.75"), existing.getAutoPartsCost());
        assertEquals(Specialization.ELECTRICIAN, existing.getSpecialization());
        assertTrue(existing.isCompleted());
        assertSame(order, existing.getOrder());
        verify(repository).save(existing);
    }

    @Test
    void updateRejectsMissingIdUnknownWorkAndInvalidWork() {
        assertThrows(IllegalArgumentException.class,
                () -> service.update(work(null, "Valid", "1.00", "2.00")));
        when(repository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.update(work(9L, "Valid", "1.00", "2.00")));
        Work invalid = work(10L, "Valid", "1.00", "2.00");
        invalid.setDescription(" ");
        assertThrows(IllegalArgumentException.class, () -> service.update(invalid));
        verify(repository, never()).save(any());
    }

    @Test
    void findAllFindByIdAndDeleteUseRepository() {
        Work work = work(2L, "Repair", "10.00", "5.00");
        when(repository.findById(2L)).thenReturn(Optional.of(work));
        when(repository.findAll()).thenReturn(List.of(work));
        assertSame(work, service.findById(2L));
        assertEquals(List.of(work), service.findAll());
        service.delete(2L);
        verify(repository).delete(work);

        when(repository.findById(8L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.findById(8L));
        assertThrows(IllegalArgumentException.class, () -> service.delete(8L));
    }

    private void assertInvalid(Work work) {
        assertThrows(IllegalArgumentException.class, () -> service.create(work));
    }

    private static Work work(Long id, String description, String workCost, String partsCost) {
        Work work = new Work();
        work.setId(id);
        work.setDescription(description);
        work.setWorkCost(workCost == null ? null : new BigDecimal(workCost));
        work.setAutoPartsCost(partsCost == null ? null : new BigDecimal(partsCost));
        work.setSpecialization(Specialization.GENERAL_SERVICE);
        return work;
    }
}
