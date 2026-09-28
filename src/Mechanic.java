public class Mechanic
{
    private String name;
    private boolean status;
    private Specialization specialization;

    public Mechanic(String name, Specialization specialization)
    {
        if (name == null)
            throw new IllegalArgumentException("Ім'я не може бути пустим");

        this.name = name;
        status = true;
        this.specialization = specialization;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void makeBusy()
    {
        if (!status)
            throw new IllegalArgumentException("Механік уже зайнятий");

        status = false;
    }

    public void makeFree()
    {
        if (status)
            throw new IllegalArgumentException("Механік уже зайнятий");

        status = true;
    }

    public String getName() {
        return name;
    }

    public boolean getStatus() {
        return status;
    }
}
