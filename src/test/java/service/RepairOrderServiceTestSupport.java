package service;

import domain.Order;
import pricing.MinOrderPriceRule;
import pricing.PricePolicy;
import pricing.ProfitPriceRule;
import pricing.UrgentPriceRule;
import repository.RepairOrderRepository;
import service.impl.RepairServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class RepairOrderServiceTestSupport {

    private RepairOrderServiceTestSupport() {
    }

    static PricePolicy defaultPricePolicy() {
        return new PricePolicy(List.of(
                new MinOrderPriceRule(),
                new UrgentPriceRule(),
                new ProfitPriceRule()));
    }

    static Fixture fixtureFor(Order order) {
        if (order.getId() == null) {
            order.setId(1L);
        }

        Map<Long, Order> savedOrders = new HashMap<>();
        savedOrders.put(order.getId(), order);

        RepairOrderRepository repository = mock(RepairOrderRepository.class);
        when(repository.findById(anyLong()))
                .thenAnswer(invocation -> Optional.ofNullable(
                        savedOrders.get(invocation.getArgument(0))));
        when(repository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrders.put(savedOrder.getId(), savedOrder);
            return savedOrder;
        });

        RepairServiceImpl service = new RepairServiceImpl(repository, defaultPricePolicy());
        return new Fixture(service, repository);
    }

    record Fixture(RepairServiceImpl service, RepairOrderRepository repository) {
    }
}
