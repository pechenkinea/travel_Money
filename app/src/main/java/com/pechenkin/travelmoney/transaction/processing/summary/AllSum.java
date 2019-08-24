package com.pechenkin.travelmoney.transaction.processing.summary;

import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.processing.CostIterable;

public class AllSum implements CostIterable {
    private int sum = 0;

    @Override
    public void iterate(Transaction transaction) {
        if (transaction.isActive() && !transaction.isRepayment()) {
            sum += transaction.getSum();
        }
    }

    @Override
    public void postIterate() {

    }

    public int getSum() {
        return sum;
    }
}
