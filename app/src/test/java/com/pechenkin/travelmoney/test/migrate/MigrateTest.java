package com.pechenkin.travelmoney.test.migrate;

import com.pechenkin.travelmoney.bd.local.helper.migrate.Migrate;

import org.junit.Assert;
import org.junit.Test;

public class MigrateTest {

    @Test
    public void migrate() {

        summatorTest(4,3);

        summatorTest(42,8);
        summatorTest(46,14);
        summatorTest(46,28);
        summatorTest(568,3);

        summatorTest(153456,7);



    }

    private void summatorTest(int sumTransaction, int members){

        System.out.println("summatorTest " + sumTransaction + " / " + members);

        double sum = (double)sumTransaction / members;
        Migrate.Sumator sumator = new Migrate.Sumator(sum);


        int resultSum = 0;
        for (int i = 0; i < members; i++) {
            int nextSum = sumator.getNext();
            System.out.print(nextSum + " ");
            resultSum += nextSum;
        }

        System.out.print("= " + resultSum);

        Assert.assertEquals(resultSum, sumTransaction);
        System.out.println();
        System.out.println();
    }


}
