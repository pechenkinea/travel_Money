package com.pechenkin.travelmoney.cost.processing;

import com.pechenkin.travelmoney.cost.Cost;


/**
 * класс для того, что бы не приходилось для разных задач несколько раз перебирать весь список трат т.к. он может быть слишком большим
 *
 */
public class ProcessIterate {
    public static void doIterate(Cost[] list, CostIterable[] costIterables) {
        for (Cost cost : list) {
            for (CostIterable costIterable : costIterables) {
                costIterable.iterate(cost);
            }
        }

        for (CostIterable costIterable : costIterables) {
            costIterable.postIterate();
        }
    }

}
