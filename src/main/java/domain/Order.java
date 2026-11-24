package domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.CREATED;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @OneToMany(mappedBy = "order")
    private List<Work> works = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechanic_id")
    private Mechanic mechanic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Priority priority;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @OrderBy("time ASC, id ASC")
    private List<Event> events = new ArrayList<>();

    @Column(name = "final_cost", precision = 12, scale = 2)
    private BigDecimal finalCost;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
