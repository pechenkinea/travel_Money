package com.pechenkin.travelmoney.test.calculation;

import com.pechenkin.travelmoney.cost.processing.CostIterable;
import com.pechenkin.travelmoney.cost.processing.ProcessIterate;
import com.pechenkin.travelmoney.cost.processing.calculation.Calculation;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.ShortCost;

import org.junit.Assert;
import org.junit.Test;

public class TestCalculation {


    @Test
    public void calculate_1() {
        Cost[] costs = new Cost[]{
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 100),
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 100),
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 100),
        };


        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(costs, new CostIterable[]{calc});
        Cost[] result = calc.getResult();


        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.length);

        Assert.assertEquals(TestMembers.MARINA, result[0].getMember());
        Assert.assertEquals(TestMembers.EVGENIY, result[0].getToMember());
        Assert.assertEquals(300, result[0].getSum(), 0);

    }

    @Test
    public void calculate_2() {

        Cost[] costs = new Cost[]{
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 100),
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 100),
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 100),
                new ShortCost(TestMembers.MARINA, TestMembers.VLAD, 100),
                new ShortCost(TestMembers.VLAD, TestMembers.EVGENIY, 100),
        };

        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(costs, new CostIterable[]{calc});
        Cost[] result = calc.getResult();

        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.length);

        Assert.assertEquals(TestMembers.MARINA, result[0].getMember());
        Assert.assertEquals(TestMembers.EVGENIY, result[0].getToMember());
        Assert.assertEquals(200, result[0].getSum(), 0);

    }

    @Test
    public void calculate_3() {

        Cost[] costs = new Cost[]{
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 100),
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 200),
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 300),

                new ShortCost(TestMembers.MARINA, TestMembers.VLAD, 100),
                new ShortCost(TestMembers.MARINA, TestMembers.VLAD, 200),
                new ShortCost(TestMembers.MARINA, TestMembers.VLAD, 300),

                new ShortCost(TestMembers.VLAD, TestMembers.MARINA, 100),
                new ShortCost(TestMembers.VLAD, TestMembers.MARINA, 500),

                new ShortCost(TestMembers.VLAD, TestMembers.VLAD, 100),
                new ShortCost(TestMembers.VLAD, TestMembers.VLAD, 100),
                new ShortCost(TestMembers.VLAD, TestMembers.VLAD, 100),

                new ShortCost(TestMembers.VLAD, TestMembers.MARINA, 100),
                new ShortCost(TestMembers.VLAD, TestMembers.MARINA, 100),

                new ShortCost(TestMembers.MARINA, TestMembers.VLAD, 200),

        };

        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(costs, new CostIterable[]{calc});
        Cost[] result = calc.getResult();

        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.length);

        Assert.assertEquals(TestMembers.MARINA, result[0].getMember());
        Assert.assertEquals(TestMembers.EVGENIY, result[0].getToMember());
        Assert.assertEquals(600, result[0].getSum(), 0);

    }

    @Test
    public void calculate_4() {

        Cost[] costs = new Cost[]{
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 100),
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 200),
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 300),

                new ShortCost(TestMembers.MARINA, TestMembers.VLAD, 100),
                new ShortCost(TestMembers.MARINA, TestMembers.VLAD, 200),
                new ShortCost(TestMembers.MARINA, TestMembers.VLAD, 300),

                new ShortCost(TestMembers.VLAD, TestMembers.MARINA, 100),
                new ShortCost(TestMembers.VLAD, TestMembers.MARINA, 500),

                new ShortCost(TestMembers.VLAD, TestMembers.VLAD, 100),
                new ShortCost(TestMembers.VLAD, TestMembers.VLAD, 100),
                new ShortCost(TestMembers.VLAD, TestMembers.VLAD, 100),

                new ShortCost(TestMembers.VLAD, TestMembers.MARINA, 100),
                new ShortCost(TestMembers.VLAD, TestMembers.MARINA, 100),
        };

        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(costs, new CostIterable[]{calc});
        Cost[] result = calc.getResult();

        Assert.assertEquals("в итоге должна быть 2 строки", 2, result.length);
        Assert.assertEquals(800, result[0].getSum() + result[1].getSum(), 0);
    }

    @Test
    public void calculate_5() {

        Cost[] costs = new Cost[1000];
        for (int i = 0; i < 1000; i = i + 2) {
            costs[i] = new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, i);
            costs[i + 1] = new ShortCost(TestMembers.MARINA, TestMembers.EVGENIY, i);
        }

        Calculation calc = new Calculation(false);
        ProcessIterate.doIterate(costs, new CostIterable[]{calc});
        Cost[] result = calc.getResult();

        Assert.assertEquals("в итоге должна быть 0 строк", 0, result.length);
    }
}
