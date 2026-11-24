package domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "mechanics")
public class Mechanic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "available", nullable = false)
    private boolean available = true;

    @ElementCollection
    @CollectionTable(name = "mechanic_specializations", joinColumns = @JoinColumn(name = "mechanic_id"))
    @Column(name = "specialization", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private List<Specialization> specializationList = new ArrayList<>();
}
