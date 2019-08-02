package com.pechenkin.travelmoney.test.calculation;

import com.pechenkin.travelmoney.calculation.Calculation;
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

        Cost[] result = Calculation.calculate(costs);

        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.length);

        Assert.assertEquals(TestMembers.MARINA, result[0].member());
        Assert.assertEquals(TestMembers.EVGENIY, result[0].to_member());
        Assert.assertEquals(300, result[0].sum(), 0);

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

        Cost[] result = Calculation.calculate(costs);

        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.length);

        Assert.assertEquals(TestMembers.MARINA, result[0].member());
        Assert.assertEquals(TestMembers.EVGENIY, result[0].to_member());
        Assert.assertEquals(200, result[0].sum(), 0);

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

        Cost[] result = Calculation.calculate(costs);

        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.length);

        Assert.assertEquals(TestMembers.MARINA, result[0].member());
        Assert.assertEquals(TestMembers.EVGENIY, result[0].to_member());
        Assert.assertEquals(600, result[0].sum(), 0);

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

        Cost[] result = Calculation.calculate(costs);

        Assert.assertEquals("в итоге должна быть 2 строки", 2, result.length);
        Assert.assertEquals(800, result[0].sum() + result[1].sum(), 0);
    }

    @Test
    public void calculate_5() {

        Cost[] costs = new Cost[1000];
        for (int i = 0; i < 1000; i = i + 2) {
            costs[i] = new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, i);
            costs[i + 1] = new ShortCost(TestMembers.MARINA, TestMembers.EVGENIY, i);
        }
        Cost[] result = Calculation.calculate(costs);
        Assert.assertEquals("в итоге должна быть 0 строк", 0, result.length);
    }
}
