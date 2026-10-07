package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mechanic {
    private String name;
    private boolean status = true;
    private List<Specialization> specializationList = new ArrayList<>();

    public Mechanic(String name, List<Specialization> specializationList ) {
        if (name == null)
            throw new IllegalArgumentException("Ім'я не може бути пустим");

        if (specializationList.isEmpty()){
            throw new IllegalStateException("Не вказані спеціалізації для механіка");
        }

        this.name = name;
        this.specializationList = specializationList;

    }

    public List<Specialization> getSpecializationList() {
        return new ArrayList<>(specializationList);
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
