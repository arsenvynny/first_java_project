package manager;

import domain.*;

import java.math.BigDecimal;
import java.util.List;

public interface RepairOrderManager {
    Order createOrder(Long carId, Priority priority);
    void addWorks(Long orderId, List<Work> works);
    void diagnose(Long orderId);
    void approve(Long orderId);
    void assignMechanic(Long orderId, Long mechanicId);
    void makeInProgress(Long orderId);
    void markWorkDone(Long workId);
    void complete(Long orderId);
    void cancel(Long orderId);

    BigDecimal getTotalOrderPrice(Long orderId);
    BigDecimal getCommercialOrderPrice(Long orderId);
}