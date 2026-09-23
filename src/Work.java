import java.math.BigDecimal;

public class Work
{
    private String description;
    private boolean status;
    private BigDecimal WorkCost;
    private BigDecimal AutoPartsCost;

    public Work(String description, boolean status, String WorkCost, String AutoPartsCost)
    {
        if (description.isEmpty())
            throw new IllegalArgumentException("Опис не може бути порожнім");
        else this.description = description;

        if (new BigDecimal(WorkCost).compareTo(BigDecimal.ZERO) < 0 )
            throw new IllegalArgumentException("Ціна не може бути від'ємною");
        else this.WorkCost = new BigDecimal(WorkCost);

        if (new BigDecimal(AutoPartsCost).compareTo(BigDecimal.ZERO) < 0 )
            throw new IllegalArgumentException("Ціна не може бути від'ємною");
        else this.AutoPartsCost = new BigDecimal(AutoPartsCost);

        this.status = status;
    }
}
