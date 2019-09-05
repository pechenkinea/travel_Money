package com.pechenkin.travelmoney.test.calculation;


import com.pechenkin.travelmoney.test.TestMembers;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.adapter.CostListItem;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.transaction.list.TotalItem;
import com.pechenkin.travelmoney.transaction.processing.CostIterable;
import com.pechenkin.travelmoney.transaction.processing.ProcessIterate;
import com.pechenkin.travelmoney.transaction.processing.calculation.Calculation;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GroupCalculation {

    @Test
    public void calculate_1() {

        List<Transaction> transactionList = new ArrayList<>();

        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.EVGENIY.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 100, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.EVGENIY.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 100, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.EVGENIY.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 100, 0))
        );

        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 0, 200))
                .addDebitItem(new DraftTransactionItem(TestMembers.EVGENIY.getMember(), 200, 0))
        );

        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 0, 200))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 200, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 0, 200))
                .addDebitItem(new DraftTransactionItem(TestMembers.EVGENIY.getMember(), 200, 0))
        );




        Calculation calc = new Calculation(true);
        ProcessIterate.doIterate(transactionList, new CostIterable[]{calc});
        List<CostListItem> groupResult = calc.getResult();


        Assert.assertEquals("в итоге должна быть 1 строка", 1, groupResult.size());

        Assert.assertEquals(TestMembers.VLAD.getMember(), ((TotalItem)groupResult.get(0)).getToMember());
        Assert.assertEquals(400, ((TotalItem)groupResult.get(0)).getSum(), 0);

    }

    @Test
    public void calculate_2() {

        List<Transaction> transactionList = new ArrayList<>();

        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.EVGENIY.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.GREEN.getMember(), 100, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.EVGENIY.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.GREEN.getMember(), 100, 0))
        );

        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.SVETA.getMember(), 0, 50))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 50, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.SVETA.getMember(), 0, 50))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 50, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.SVETA.getMember(), 0, 50))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 50, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.SVETA.getMember(), 0, 50))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 50, 0))
        );




        Calculation calc = new Calculation(true);
        ProcessIterate.doIterate(transactionList, new CostIterable[]{calc});
        List<CostListItem> groupResult = calc.getResult();


        Assert.assertEquals("в итоге должна быть 0 строк", 0, groupResult.size());
    }

}
