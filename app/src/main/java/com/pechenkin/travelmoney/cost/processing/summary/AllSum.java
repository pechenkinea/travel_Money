package com.pechenkin.travelmoney.cost.processing.summary;

import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.processing.CostIterable;

public class AllSum implements CostIterable {
    private double sum = 0;

    @Override
    public void iterate(Cost cost) {
        if (cost.isActive()) {
            sum += cost.getSum();
        }
    }

    @Override
    public void postIterate() {

    }

    public double getSum() {
        return sum;
    }
}
