import java.math.BigDecimal;

public class Work
{
    private String description;
    private boolean status;
    private BigDecimal workCost;
    private BigDecimal autoPartsCost;

    public Work(String description, boolean status, String workCost, String autoPartsCost)
    {
        if (description == null)
            throw new IllegalArgumentException("Опис не може бути порожнім");

        if (workCost == null || autoPartsCost == null)
            throw new IllegalArgumentException("Ціни не можуть бути пустими");

        if (workCost.matches(".*[^0-9.].*"))
            throw new IllegalArgumentException("Введена некоректна ціна");

        if (new BigDecimal(workCost).compareTo(BigDecimal.ZERO) < 0 )
            throw new IllegalArgumentException("Ціна не може бути від'ємною");

        if (new BigDecimal(autoPartsCost).compareTo(BigDecimal.ZERO) < 0 )
            throw new IllegalArgumentException("Ціна не може бути від'ємною");

        this.autoPartsCost = new BigDecimal(autoPartsCost);
        this.workCost = new BigDecimal(workCost);
        this.description = description;
        this.status = status;
    }

    public BigDecimal getWorkCost() {
        return workCost;
    }

    public boolean isStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAutoPartsCost() {
        return autoPartsCost;
    }
}
