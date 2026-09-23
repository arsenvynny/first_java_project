public class Client
{
    private String name;
    private String SureName;

    public Client(String name, String SureName)
    {
        if (name.isEmpty() || SureName.isEmpty())
            throw new IllegalArgumentException("Ім'я або прізвище не може бути пустим!");
        else {
            this.name = name;
            this.SureName = SureName;
        }
    }
}
