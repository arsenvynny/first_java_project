import javax.management.BadAttributeValueExpException;

public class Car
{
    private  String model;
    private  String mark;
    private  int year;
    private  Client owner;
    private  String vinCode;

    public String getVinCode() {
        return vinCode;
    }

    public Client getOwner() {
        return owner;
    }

    public String getMark() {
        return mark;
    }

    public int getYear() {
        return year;
    }

    public String getModel() {
        return model;
    }

    public Car(String mark, String model, int year, Client owner, String vinCode)
    {
        if (vinCode == null)
            throw new IllegalArgumentException("Недостовірний VinCode");

        if (year < 1981 || year > 2026)
            throw new IllegalArgumentException("Рік випуску недостовірний");

        this.vinCode = vinCode;
        this.year = year;
        this.model = model;
        this.mark = mark;
        this.owner = owner;
    }
}
