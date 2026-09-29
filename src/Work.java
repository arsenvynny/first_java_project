import java.math.BigDecimal;

public class Work
{
    private String description;
    private boolean isCompleted;
    private BigDecimal workCost;
    private BigDecimal autoPartsCost;


    public Work(String description, String workCost, String autoPartsCost)
    {
        if (description == null)
            throw new IllegalArgumentException("Опис не може бути порожнім");

        if (workCost == null || autoPartsCost == null)
            throw new IllegalArgumentException("Ціни не можуть бути пустими");

        if (new BigDecimal(workCost).compareTo(BigDecimal.ZERO) < 0 )
            throw new IllegalArgumentException("Ціна не може бути від'ємною");

        if (new BigDecimal(autoPartsCost).compareTo(BigDecimal.ZERO) < 0 )
            throw new IllegalArgumentException("Ціна не може бути від'ємною");

        this.autoPartsCost = new BigDecimal(autoPartsCost);
        this.workCost = new BigDecimal(workCost);
        this.description = description;
        isCompleted = false;
    }

    public void makeDone()
    {
        isCompleted = true;
    }


    public BigDecimal getWorkCost() { return workCost; }

    public boolean isCompleted() { return isCompleted; }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAutoPartsCost() {
        return autoPartsCost;
    }
}
