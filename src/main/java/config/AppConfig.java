package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import pricing.MinOrderPriceRule;
import pricing.PricePolicy;
import pricing.ProfitPriceRule;
import pricing.UrgentPriceRule;
import repository.RepairOrderRepository;
import service.impl.RepairServiceImpl;

import java.util.List;

@Configuration
public class AppConfig {
}
