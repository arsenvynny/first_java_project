package domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "works")
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "completed", nullable = false)
    private boolean isCompleted;

    @Column(name = "work_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal workCost;

    @Column(name = "auto_parts_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal autoPartsCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Specialization specialization;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
