package domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {
    private Long id;
    private OrderStatus status = OrderStatus.CREATED;
    private Car car;
    private List<Work> works = new ArrayList<>();
    private Mechanic mechanic;
    private Priority priority;
    private List<Event> events = new ArrayList<>();

    public Order(Car car, Priority priority) {
        this.car = car;
        this.priority = priority;
    }
}


