package domain.ClientTest;

import domain.Client;
import org.junit.jupiter.api.Test;
import domain.Mechanic;
import domain.Specialization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryRepairOrderRepository;
import repository.RepairOrderRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreatingClientTest {

    @Test
    void Client_creatingClient_isCreated(){

        String name = "Arsen";
        String surName = "Vynnytskyi";

        Client client = new Client(name, surName);

        assertEquals(name, client.getName());
        assertEquals(surName, client.getSureName());
    }

    @Test
    void Client_creatingClientWithNullName_ExceptionThrows(){

        String name = null;
        String surName = "Vynnytskyi";

        assertThrows(IllegalArgumentException.class,
                () -> new Client(name, surName));

        String name2 = "";
        assertThrows(IllegalArgumentException.class,
                () -> new Client(name2, surName));
    }

    @Test
    void Client_creatingClientWithNullSurName_ExceptionThrows(){

        String name = "Arsen";
        String surName = null;

        assertThrows(IllegalArgumentException.class,
                () -> new Client(name, surName));

        String surName2 = "";
        assertThrows(IllegalArgumentException.class,
                () -> new Client(name, surName2));
    }
}
