package domain.mechanic;

import domain.Mechanic;
import domain.Specialization;
import domain.Work;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CanPerformTest {

    Mechanic mechanic;

    @BeforeEach
    void setUp() {

        List<Specialization> specializations = new ArrayList<>();

        specializations.add(Specialization.GENERAL_SERVICE);
        specializations.add(Specialization.CHASSIS);

        String name = "Arsen";
        mechanic = new Mechanic(name, specializations);
    }

    @ParameterizedTest
    @EnumSource(
            value = Specialization.class,
            names = {"CHASSIS", "GENERAL_SERVICE"}

    )
    void canPerform_TryToPerformWorks_isPerform(Specialization specialization) {

        Work work = new Work("change wheels",
                "100",
                "200",
                specialization);

        assertTrue(mechanic.canPerform(work));
    }

    @ParameterizedTest
    @EnumSource(
            value = Specialization.class,
            names = {"CHASSIS", "GENERAL_SERVICE"},
            mode = EnumSource.Mode.EXCLUDE

    )
    void canPerform_TryToPerformWorksWithoutAppropriateSpecializations_isPerform(
            Specialization specialization) {

        Work work = new Work("change wheels",
                "100",
                "200",
                specialization);

        assertFalse(mechanic.canPerform(work));
    }
}
