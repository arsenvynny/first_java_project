import java.util.Objects;

public class Event {
    String description;
    String time;
    String type;

    public Event(String description, String time, String type)
    {
        if (description == null || time == null || type == null)
            throw new IllegalArgumentException("Поля не можуть бути пустими");

        this.description = description;
        this.time = time;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return  Objects.equals(getDescription(), event.getDescription()) &&
                Objects.equals(getTime(), event.getTime()) &&
                Objects.equals(getType(), event.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getTime(), getType());
    }
}
