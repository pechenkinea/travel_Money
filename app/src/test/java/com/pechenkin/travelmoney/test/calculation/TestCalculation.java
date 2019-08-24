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

public class TestCalculation {


    @Test
    public void calculate_1() {


        TripForTest tripForTest = new TripForTest();

        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 100));



        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(tripForTest.getAllCost(), new CostIterable[]{calc});
        List<ShortCost> result = calc.getResult();


        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.size());

        Assert.assertEquals(TestMembers.MARINA.getMember(), result.get(0).getMember());
        Assert.assertEquals(TestMembers.EVGENIY.getMember(), result.get(0).getToMember());
        Assert.assertEquals(300, result.get(0).getSum(), 0);

    }

    @Test
    public void calculate_2() {

        TripForTest tripForTest = new TripForTest();

        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 100));

        tripForTest.addCost(new CostForTest(TestMembers.MARINA.getMember(), TestMembers.VLAD.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.EVGENIY.getMember(), 100));



        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(tripForTest.getAllCost(), new CostIterable[]{calc});
        List<ShortCost> result = calc.getResult();


        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.size());

        Assert.assertEquals(TestMembers.MARINA.getMember(), result.get(0).getMember());
        Assert.assertEquals(TestMembers.EVGENIY.getMember(), result.get(0).getToMember());
        Assert.assertEquals(200, result.get(0).getSum(), 0);

    }

    @Test
    public void calculate_3() {

        TripForTest tripForTest = new TripForTest();

        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 200));
        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 300));

        tripForTest.addCost(new CostForTest(TestMembers.MARINA.getMember(), TestMembers.VLAD.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.MARINA.getMember(), TestMembers.VLAD.getMember(), 200));
        tripForTest.addCost(new CostForTest(TestMembers.MARINA.getMember(), TestMembers.VLAD.getMember(), 300));

        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.MARINA.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.MARINA.getMember(), 500));

        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.VLAD.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.VLAD.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.VLAD.getMember(), 100));

        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.MARINA.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.MARINA.getMember(), 100));

        tripForTest.addCost(new CostForTest(TestMembers.MARINA.getMember(), TestMembers.VLAD.getMember(), 200));



        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(tripForTest.getAllCost(), new CostIterable[]{calc});
        List<ShortCost> result = calc.getResult();


        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.size());

        Assert.assertEquals(TestMembers.MARINA.getMember(), result.get(0).getMember());
        Assert.assertEquals(TestMembers.EVGENIY.getMember(), result.get(0).getToMember());
        Assert.assertEquals(600, result.get(0).getSum(), 0);

    }

    @Test
    public void calculate_4() {

        TripForTest tripForTest = new TripForTest();

        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 200));
        tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), 300));

        tripForTest.addCost(new CostForTest(TestMembers.MARINA.getMember(), TestMembers.VLAD.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.MARINA.getMember(), TestMembers.VLAD.getMember(), 200));
        tripForTest.addCost(new CostForTest(TestMembers.MARINA.getMember(), TestMembers.VLAD.getMember(), 300));

        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.MARINA.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.MARINA.getMember(), 500));

        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.VLAD.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.VLAD.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.VLAD.getMember(), 100));

        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.MARINA.getMember(), 100));
        tripForTest.addCost(new CostForTest(TestMembers.VLAD.getMember(), TestMembers.MARINA.getMember(), 100));


        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(tripForTest.getAllCost(), new CostIterable[]{calc});
        List<ShortCost> result = calc.getResult();


        Assert.assertEquals("в итоге должна быть 2 строки", 2, result.size());
        Assert.assertEquals(800, result.get(0).getSum() + result.get(1).getSum(), 0);
    }

    @Test
    public void calculate_5() {
        TripForTest tripForTest = new TripForTest();

        Cost[] costs = new Cost[1000];
        for (int i = 0; i < 1000; i = i + 2) {

            tripForTest.addCost(new CostForTest(TestMembers.EVGENIY.getMember(), TestMembers.MARINA.getMember(), i));
            tripForTest.addCost(new CostForTest(TestMembers.MARINA.getMember(), TestMembers.EVGENIY.getMember(), i));
        }

        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(tripForTest.getAllCost(), new CostIterable[]{calc});
        List<ShortCost> result = calc.getResult();

        Assert.assertEquals("в итоге должна быть 0 строк", 0, result.size());
    }
}
