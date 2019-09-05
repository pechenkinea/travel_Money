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

public class TestCalculation {


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




        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(transactionList, new CostIterable[]{calc});
        List<CostListItem> result = calc.getResult();


        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.size());

        Assert.assertEquals(TestMembers.MARINA.getMember(), ((TotalItem)result.get(0)).getMember());
        Assert.assertEquals(TestMembers.EVGENIY.getMember(), ((TotalItem)result.get(0)).getToMember());
        Assert.assertEquals(300, ((TotalItem)result.get(0)).getSum(), 0);

    }

    @Test
    public void calculate_2() {

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
                .addCreditItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 100, 0))
        );

        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.EVGENIY.getMember(), 100, 0))
        );



        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(transactionList, new CostIterable[]{calc});
        List<CostListItem> result = calc.getResult();


        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.size());

        Assert.assertEquals(TestMembers.MARINA.getMember(), ((TotalItem)result.get(0)).getMember());
        Assert.assertEquals(TestMembers.EVGENIY.getMember(), ((TotalItem)result.get(0)).getToMember());
        Assert.assertEquals(200, ((TotalItem)result.get(0)).getSum(), 0);

    }

    @Test
    public void calculate_3() {

        List<Transaction> transactionList = new ArrayList<>();

        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.EVGENIY.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 100, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.EVGENIY.getMember(), 0, 200))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 200, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.EVGENIY.getMember(), 0, 300))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 300, 0))
        );

        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 100, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 0, 200))
                .addDebitItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 200, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 0, 300))
                .addDebitItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 300, 0))
        );

        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 100, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 0, 500))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 500, 0))
        );

        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 100, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 100, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 100, 0))
        );

        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 100, 0))
        );
        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 0, 100))
                .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 100, 0))
        );

        transactionList.add(new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 0, 200))
                .addDebitItem(new DraftTransactionItem(TestMembers.VLAD.getMember(), 200, 0))
        );


        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(transactionList, new CostIterable[]{calc});
        List<CostListItem> result = calc.getResult();


        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.size());

        Assert.assertEquals(TestMembers.MARINA.getMember(), ((TotalItem)result.get(0)).getMember());
        Assert.assertEquals(TestMembers.EVGENIY.getMember(), ((TotalItem)result.get(0)).getToMember());
        Assert.assertEquals(600, ((TotalItem)result.get(0)).getSum(), 0);

    }


    @Test
    public void calculate_5() {
        List<Transaction> transactionList = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {

            transactionList.add(new DraftTransaction()
                    .addCreditItem(new DraftTransactionItem(TestMembers.EVGENIY.getMember(), 0, i))
                    .addDebitItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), i, 0))
            );
            transactionList.add(new DraftTransaction()
                    .addCreditItem(new DraftTransactionItem(TestMembers.MARINA.getMember(), 0, i))
                    .addDebitItem(new DraftTransactionItem(TestMembers.EVGENIY.getMember(), i, 0))
            );
        }

        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(transactionList, new CostIterable[]{calc});
        List<CostListItem> result = calc.getResult();

        Assert.assertEquals("в итоге должна быть 0 строк", 0, result.size());
    }
}
