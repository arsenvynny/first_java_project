import java.util.Objects;

public class Client
{
    private String name;
    private String surName;

    public Client(String name, String sureName)
    {
        if (name == null)
            throw new IllegalArgumentException("Ім'я не може бути пустим!");

        if (sureName == null)
            throw new IllegalArgumentException("Прізвище не може бути пустим!");

        this.name = name;
        this.surName = sureName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(getName(), client.getName()) && Objects.equals(surName, client.surName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), surName);
    }

    public String getName() {
        return name;
    }

    public String getSureName() {
        return surName;
    }
}
