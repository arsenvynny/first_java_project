import javax.management.BadAttributeValueExpException;

public class Car
{
    private  String model;
    private  String mark;
    private  int year;
    private  Client owner;
    private  String VinCode;

    public Car(String mark, String model, int year, Client owner, String VinCode)
    {
        String FinalVinCode = VinCode.replace(" ", "");

        if (FinalVinCode.length() != 17)
            throw new IllegalArgumentException("Недостовірний VinCode");
        else this.VinCode = FinalVinCode;

        if (year < 1981 || year > 2026)
            throw new IllegalArgumentException("Рік випуску недостовірний");
        else this.year = year;

        this.model = model;
        this.mark = mark;
        this.owner = owner;
    }
}
