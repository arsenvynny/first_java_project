package service.impl;

import domain.Mechanic;
import domain.Specialization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.MechanicRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MechanicServiceImplTest {
    @Mock private MechanicRepository repository;
    private MechanicServiceImpl service;

    @BeforeEach
    void setUp() { service = new MechanicServiceImpl(repository); }

    @Test
    void createMarksMechanicAvailable() {
        Mechanic mechanic = mechanic(null, "Alex", Specialization.ENGINE_REPAIR);
        mechanic.setAvailable(false);
        when(repository.save(mechanic)).thenReturn(mechanic);
        assertSame(mechanic, service.create(mechanic));
        assertTrue(mechanic.isAvailable());
        verify(repository).save(mechanic);
    }

    @Test
    void updateCopiesEditableFieldsButPreservesAvailability() {
        Mechanic existing = mechanic(6L, "Old", Specialization.ENGINE_REPAIR);
        existing.setAvailable(false);
        Mechanic changes = mechanic(6L, "New", Specialization.ELECTRICIAN);
        when(repository.findById(6L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        assertSame(existing, service.update(changes));
        assertEquals("New", existing.getName());
        assertEquals(List.of(Specialization.ELECTRICIAN), existing.getSpecializationList());
        assertFalse(existing.isAvailable());
        verify(repository).save(existing);
    }

    @Test
    void createRejectsIdAndInvalidMechanics() {
        assertThrows(IllegalArgumentException.class,
                () -> service.create(mechanic(1L, "Alex", Specialization.ENGINE_REPAIR)));
        assertInvalid(null);
        assertInvalid(mechanic(null, null, Specialization.ENGINE_REPAIR));
        assertInvalid(mechanic(null, " ", Specialization.ENGINE_REPAIR));
        assertInvalid(mechanic(null, "n".repeat(51), Specialization.ENGINE_REPAIR));
        Mechanic noList = mechanic(null, "Alex", Specialization.ENGINE_REPAIR);
        noList.setSpecializationList(null);
        assertInvalid(noList);
        Mechanic emptyList = mechanic(null, "Alex", Specialization.ENGINE_REPAIR);
        emptyList.setSpecializationList(List.of());
        assertInvalid(emptyList);
        Mechanic nullMember = mechanic(null, "Alex", Specialization.ENGINE_REPAIR);
        nullMember.setSpecializationList(java.util.Arrays.asList(Specialization.ENGINE_REPAIR, null));
        assertInvalid(nullMember);
        Mechanic duplicate = mechanic(null, "Alex", Specialization.ENGINE_REPAIR);
        duplicate.setSpecializationList(List.of(Specialization.ENGINE_REPAIR, Specialization.ENGINE_REPAIR));
        assertInvalid(duplicate);
        verifyNoInteractions(repository);
    }

    @Test
    void updateRejectsMissingIdAndUnknownMechanic() {
        assertThrows(IllegalArgumentException.class,
                () -> service.update(mechanic(null, "Alex", Specialization.ENGINE_REPAIR)));
        when(repository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.update(mechanic(9L, "Alex", Specialization.ENGINE_REPAIR)));
        verify(repository, never()).save(any());
    }

    @Test
    void readsAndDeletesMechanics() {
        Mechanic mechanic = mechanic(2L, "Alex", Specialization.CHASSIS);
        when(repository.findById(2L)).thenReturn(Optional.of(mechanic));
        when(repository.findAll()).thenReturn(List.of(mechanic));
        assertSame(mechanic, service.findById(2L));
        assertEquals(List.of(mechanic), service.findAll());
        service.delete(2L);
        verify(repository).delete(mechanic);

        when(repository.findById(8L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.findById(8L));
        assertThrows(IllegalArgumentException.class, () -> service.delete(8L));
    }

    private void assertInvalid(Mechanic mechanic) {
        assertThrows(IllegalArgumentException.class, () -> service.create(mechanic));
    }

    private static Mechanic mechanic(Long id, String name, Specialization specialization) {
        Mechanic mechanic = new Mechanic();
        mechanic.setId(id);
        mechanic.setName(name);
        mechanic.setSpecializationList(new ArrayList<>(List.of(specialization)));
        return mechanic;
    }
}
