package domain;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Mechanic {
    private Long id;
    private String name;
    private boolean status = true;
    private List<Specialization> specializationList = new ArrayList<>();

    public Mechanic(String name, List<Specialization> specializationList) {
        if (name == null)
            throw new IllegalArgumentException("The mechanic name cannot be null");

        if (specializationList == null) {
            throw new IllegalArgumentException("Specializations must be specified for the mechanic");
        }

        if (specializationList.isEmpty()) {
            throw new IllegalArgumentException("Specializations must be specified for the mechanic");
        }

        this.name = name;
        this.specializationList = specializationList;

    }

    public void makeBusy() {
        if (!status)
            throw new IllegalStateException("The mechanic is already busy");

        status = false;
    }

    public void makeFree() {
        if (status)
            throw new IllegalStateException("The mechanic is already available");

        status = true;
    }

    public boolean canPerform(Work work) {

        for (Specialization specialization : specializationList) {
            if (work.getSpecialization() == specialization) {
                return true;
            }
        }
        return false;
    }

    public boolean isAvailable() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Mechanic mechanic = (Mechanic) o;
        return Objects.equals(getName(), mechanic.getName()) &&
                Objects.equals(getSpecializationList(), mechanic.getSpecializationList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSpecializationList());
    }
}
