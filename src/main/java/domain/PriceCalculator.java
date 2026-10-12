package domain;

import java.math.BigDecimal;

public class PriceCalculator {

    private Order order;

    public PriceCalculator (final Order order){

        if (order.getWork() == null){
            throw new IllegalStateException("Відсутній список робіт для підрахування вартості замовлення");
        }

        this.order = order;
    }

    public BigDecimal getWorkPrice(){

        BigDecimal totalWorkPrice = BigDecimal.ZERO;

        for (Work work : order.getWork()){
            totalWorkPrice = totalWorkPrice.add(work.getWorkCost());
        }

        if (order.getPriority() == Priority.URGED){
            return totalWorkPrice.multiply(Constants.Urgent.FEE);
        }

        return totalWorkPrice;
    }

    public BigDecimal getCarPartsPrice(){

        BigDecimal totalCarPartsPrice = BigDecimal.ZERO;

        for (Work work : order.getWork()){
            totalCarPartsPrice = totalCarPartsPrice.add(work.getWorkCost());
        }

        return totalCarPartsPrice;
    }

    public BigDecimal getTotalOrderPrice(){
        return getWorkPrice().add(getCarPartsPrice());
    }
}
