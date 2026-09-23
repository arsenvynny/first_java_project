public class Client
{
    private String name;
    private String sureName;

    public Client(String name, String sureName)
    {
        if (name == null)
            throw new IllegalArgumentException("Ім'я не може бути пустим!");

        if (sureName == null)
            throw new IllegalArgumentException("Прізвище не може бути пустим!");

        this.name = name;
        this.sureName = sureName;
    }

    public String getName() {
        return name;
    }

    public String getSureName() {
        return sureName;
    }
}
