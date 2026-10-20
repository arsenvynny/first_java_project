package domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Work
{
    private String description;
    private boolean isCompleted = false;
    private BigDecimal workCost;
    private BigDecimal autoPartsCost;
    private Specialization specialization;


    public Work(String description, String workCost, String autoPartsCost, Specialization specialization)
    {
        if (description == null)
            throw new IllegalArgumentException("The work description cannot be empty");

        if (description.isEmpty())
            throw new IllegalArgumentException("The work description cannot be empty");

        if (workCost == null)
            throw new IllegalArgumentException("Work cost cannot be empty");

        if (workCost.isEmpty())
            throw new IllegalArgumentException("Work cost cannot be empty");

        if (autoPartsCost == null)
            throw new IllegalArgumentException("Parts cost cannot be empty");

        if (autoPartsCost.isEmpty())
            throw new IllegalArgumentException("Parts cost cannot be empty");

        if (new BigDecimal(workCost).compareTo(BigDecimal.ZERO) < 0 )
            throw new IllegalArgumentException("The cost cannot be negative");

        if (new BigDecimal(autoPartsCost).compareTo(BigDecimal.ZERO) < 0 )
            throw new IllegalArgumentException("The cost cannot be negative");

        this.autoPartsCost = new BigDecimal(autoPartsCost);
        this.workCost = new BigDecimal(workCost);
        this.description = description;
        this.specialization = specialization;
    }

    public void makeDone()
    {
        if (isCompleted)
            throw new IllegalStateException("This work item has already been completed");

        isCompleted = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Work work = (Work) o;
        return  Objects.equals(description, work.description) &&
                Objects.equals(workCost, work.workCost) &&
                Objects.equals(autoPartsCost, work.autoPartsCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, workCost, autoPartsCost);
    }

    public Specialization getSpecialization() {
        return specialization;
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
