package domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;



public class OrderTest {
    private Order order;

    @BeforeEach
    void setOrder(){
        order = new Order();

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
