package domain;

import java.util.Objects;

public class Mechanic {
    private String name;
    private boolean status = true;
    private Specialization specialization;

    public Mechanic(String name, Specialization specialization) {
        if (name == null)
            throw new IllegalArgumentException("Ім'я не може бути пустим");

        this.name = name;
        this.specialization = specialization;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void makeBusy() {
        if (!status)
            throw new IllegalStateException("Механік уже зайнятий");

        status = false;
    }

    public void makeFree() {
        if (status)
            throw new IllegalStateException("Механік уже вільний");

        status = true;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Mechanic mechanic = (Mechanic) o;
        return Objects.equals(getName(), mechanic.getName()) &&
                getSpecialization() == mechanic.getSpecialization();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSpecialization());
    }
}
