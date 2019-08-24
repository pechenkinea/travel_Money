package com.pechenkin.travelmoney.transaction.processing;

import com.pechenkin.travelmoney.transaction.Transaction;

import java.util.List;


/**
 * класс для того, что бы не приходилось для разных задач несколько раз перебирать весь список трат т.к. он может быть слишком большим
 *
 */
public class ProcessIterate {
    public static void doIterate(List<Transaction> list, CostIterable[] costIterables) {
        for (Transaction transaction : list) {
            for (CostIterable costIterable : costIterables) {
                costIterable.iterate(transaction);
            }
        }

        for (CostIterable costIterable : costIterables) {
            costIterable.postIterate();
        }
    }

}
