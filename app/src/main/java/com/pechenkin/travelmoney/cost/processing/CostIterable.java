package com.pechenkin.travelmoney.cost.processing;

import com.pechenkin.travelmoney.cost.Cost;

public interface CostIterable {

    void iterate(Cost item);
    void postIterate();

}
