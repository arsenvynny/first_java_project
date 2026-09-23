public class Mechanic
{
    private String name;
    private boolean isAvailable;
    private Specialization specialization;

    public Mechanic(String name, Specialization specialization)
    {
        if (name == null)
            throw new IllegalArgumentException("Ім'я не може бути пустим");

        this.name = name;
        isAvailable = true;
        this.specialization = specialization;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
