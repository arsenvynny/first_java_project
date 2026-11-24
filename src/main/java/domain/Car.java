package domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "mark", nullable = false)
    private String mark;

    @Column(name = "year", nullable = false)
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Client owner;

    @Column(name = "vin_code", nullable = false, unique = true)
    private String vinCode;
}
