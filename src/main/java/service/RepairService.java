package service;

import domain.*;

import java.math.BigDecimal;
import java.util.List;

public interface RepairService {
    Order create(Order order);
    void addWorks(Long orderId, List<Long> workIds);
    void markWorkDone(Long workId);
    boolean isWorkCompleted(Long workId);
    void assignMechanic(Long orderId, Long mechanicId);
    void diagnose(Long orderId);
    void approve(Long orderId);
    void makeInProgress(Long orderId);
    void complete(Long orderId);
    void cancel(Long orderId);
    BigDecimal getWorkPrice(Order order);
    BigDecimal getPartsPrice(Order order);
    BigDecimal getTotalOrderPrice(Order order);
    BigDecimal getCommercialOrderPrice(Order order);
}
