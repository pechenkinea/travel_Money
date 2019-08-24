package com.pechenkin.travelmoney.test.calculation;

import com.pechenkin.travelmoney.transaction.processing.CostIterable;
import com.pechenkin.travelmoney.transaction.processing.ProcessIterate;
import com.pechenkin.travelmoney.transaction.processing.calculation.Calculation;
import com.pechenkin.travelmoney.test.CostForTest;
import com.pechenkin.travelmoney.test.TestMembers;
import com.pechenkin.travelmoney.test.TripForTest;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class GroupCalculation {

    @Test
    public void calculate_1() {

        TripForTest tripForTest = new TripForTest();

        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 100));

        tripForTest.addCost(new CostForTest(TestMembers.MARINA.getMember(), TestMembers.EVGENIY.getMember(), 200));

        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.MARINA.getMember(), 200));
        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.EVGENIY.getMember(), 200));


        Calculation calc = new Calculation(true);
        ProcessIterate.doIterate(tripForTest.getAllCost(), new CostIterable[]{calc});
        List<ShortCost> groupResult = calc.getResult();


        Assert.assertEquals("в итоге должна быть 1 строка", 1, groupResult.size());

        Assert.assertEquals(TestMembers.VLAD.getMember(), groupResult.get(0).getToMember());
        Assert.assertEquals(400, groupResult.get(0).getSum(), 0);

    }

    @Test
    public void calculate_2() {

        TripForTest tripForTest = new TripForTest();

        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.GREEN.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.GREEN.getMember(), 100));

        tripForTest.addCost(new CostForTest(TestMembers.SVETA.getMember(), TestMembers.MARINA.getMember(), 50));
        tripForTest.addCost(new CostForTest(TestMembers.SVETA.getMember(), TestMembers.MARINA.getMember(), 50));
        tripForTest.addCost(new CostForTest(TestMembers.SVETA.getMember(), TestMembers.MARINA.getMember(), 50));
        tripForTest.addCost(new CostForTest(TestMembers.SVETA.getMember(), TestMembers.MARINA.getMember(), 50));


        Calculation calc = new Calculation(true);
        ProcessIterate.doIterate(tripForTest.getAllCost(), new CostIterable[]{calc});
        List<ShortCost> groupResult = calc.getResult();


        Assert.assertEquals("в итоге должна быть 0 строк", 0, groupResult.size());
    }

}
