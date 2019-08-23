package com.pechenkin.travelmoney.cost.processing.summary;

import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.processing.CostIterable;

public class AllSum implements CostIterable {
    private int sum = 0;

    @Override
    public void iterate(Cost cost) {
        if (cost.isActive() && !cost.isRepayment()) {
            sum += cost.getSum();
        }
    }

    @Override
    public void postIterate() {

    }

    public int getSum() {
        return sum;
    }
}
