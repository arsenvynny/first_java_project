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

    public String getName() {
        return name;
    }

    public String getSureName() {
        return surName;
    }
}
