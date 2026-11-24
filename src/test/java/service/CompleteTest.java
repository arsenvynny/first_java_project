package service;

import domain.EventType;
import domain.Mechanic;
import domain.Order;
import domain.OrderStatus;
import domain.Priority;
import domain.Work;
import org.junit.jupiter.api.Test;
import pricing.MinOrderPriceRule;
import pricing.PricePolicy;
import pricing.ProfitPriceRule;
import pricing.UrgentPriceRule;
import service.impl.RepairServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CompleteTest {

}
