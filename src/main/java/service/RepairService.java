package service;

import domain.*;

import java.math.BigDecimal;

public interface RepairService {
     void addWork(Order order, Work work);
     void assignMechanic(Order order, Mechanic mechanic);
     void diagnose(Order order);
     void approve(Order order);
     void makeInProgress(Order order);
     void complete(Order order);
     void cancel(Order order);
    BigDecimal getWorkPrice(Order order);
    BigDecimal getPartsPrice(Order order);
    BigDecimal getTotalOrderPrice(Order order);
    BigDecimal getCommercialOrderPrice(Order order);
}
