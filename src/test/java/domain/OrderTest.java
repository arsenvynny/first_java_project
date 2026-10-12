package domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;



public class OrderTest {
    private RepairOrder order;

    @BeforeEach
    void setOrder(){
        Client client = new Client("Arsen", "Vynnytskyi");
        Car car = new Car("Mazda", "RX-7", 2026, client, "BC0000KA");
        order = new RepairOrder(car, Priority.NORMAL);

        Work work1 = new Work("tttt", "100", "200", Specialization.ELECTRICIAN);
        Work work2 = new Work("tttt", "100", "200", Specialization.ELECTRICIAN);

        order.addWork(work1);
        order.addWork(work2);
    }

    @Test
    void OrderTest(){
        List<Specialization> specializationList = new ArrayList<>();
        specializationList.add(Specialization.ELECTRICIAN);


        Mechanic mechanic1 = new Mechanic("Arsen", specializationList);

        order.assignMechanic(mechanic1);
    }
}
