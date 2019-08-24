package com.pechenkin.travelmoney.transaction.processing;

import com.pechenkin.travelmoney.transaction.Transaction;

public interface CostIterable {

    void iterate(Transaction item);
    void postIterate();

}
