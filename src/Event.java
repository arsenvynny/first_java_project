import java.time.LocalDateTime;
import java.util.Objects;

public class Event {
    private String description;
    private LocalDateTime time;
    private EventType type;

    public Event(String description, EventType type)
    {
        if (description == null)
            throw new IllegalArgumentException("Опис не може бути пустими");

        this.description = description;
        this.type = type;
        time = LocalDateTime.now();
    }

    public EventType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTime() {
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
