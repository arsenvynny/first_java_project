package domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
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

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Client owner;

    @Column(name = "vin_code")
    private String vinCode;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return getYear() == car.getYear() &&
                Objects.equals(getModel(), car.getModel()) &&
                Objects.equals(getMark(), car.getMark()) &&
                Objects.equals(getOwner(), car.getOwner()) &&
                Objects.equals(getVinCode(), car.getVinCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getModel(),
                            getMark(),
                            getYear(),
                            getOwner(),
                            getVinCode());
    }
}
