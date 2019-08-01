package com.pechenkin.travelmoney.test.calculation;

import com.pechenkin.travelmoney.calculation.Calculation;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.ShortCost;

import org.junit.Assert;
import org.junit.Test;

public class TestCalculation {

    private static int EVGENY = 0;
    private static int MARINA = 1;
    private static int VALD = 2;

    @Test
    public void calculate_1() {
        Cost[] costs = new Cost[]{
                new ShortCost(EVGENY, MARINA, 100),
                new ShortCost(EVGENY, MARINA, 100),
                new ShortCost(EVGENY, MARINA, 100),
        };

        Cost[] result = Calculation.calculate(costs);

        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.length);

        Assert.assertEquals(MARINA, result[0].member());
        Assert.assertEquals(EVGENY, result[0].to_member());
        Assert.assertEquals(300, result[0].sum(), 0);

    }

    @Test
    public void calculate_2() {

        Cost[] costs = new Cost[]{
                new ShortCost(EVGENY, MARINA, 100),
                new ShortCost(EVGENY, MARINA, 100),
                new ShortCost(EVGENY, MARINA, 100),
                new ShortCost(MARINA, VALD, 100),
                new ShortCost(VALD, EVGENY, 100),
        };

        Cost[] result = Calculation.calculate(costs);

        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.length);

        Assert.assertEquals(MARINA, result[0].member());
        Assert.assertEquals(EVGENY, result[0].to_member());
        Assert.assertEquals(200, result[0].sum(), 0);

    }

    @Test
    public void calculate_3() {

        Cost[] costs = new Cost[]{
                new ShortCost(EVGENY, MARINA, 100),
                new ShortCost(EVGENY, MARINA, 200),
                new ShortCost(EVGENY, MARINA, 300),

                new ShortCost(MARINA, VALD, 100),
                new ShortCost(MARINA, VALD, 200),
                new ShortCost(MARINA, VALD, 300),

                new ShortCost(VALD, MARINA, 100),
                new ShortCost(VALD, MARINA, 500),

                new ShortCost(VALD, VALD, 100),
                new ShortCost(VALD, VALD, 100),
                new ShortCost(VALD, VALD, 100),

                new ShortCost(VALD, MARINA, 100),
                new ShortCost(VALD, MARINA, 100),

                new ShortCost(MARINA, VALD, 200),

        };

        Cost[] result = Calculation.calculate(costs);

        Assert.assertEquals("в итоге должна быть 1 строка", 1, result.length);

        Assert.assertEquals(MARINA, result[0].member());
        Assert.assertEquals(EVGENY, result[0].to_member());
        Assert.assertEquals(600, result[0].sum(), 0);

    }

    @Test
    public void calculate_4() {

        Cost[] costs = new Cost[]{
                new ShortCost(EVGENY, MARINA, 100),
                new ShortCost(EVGENY, MARINA, 200),
                new ShortCost(EVGENY, MARINA, 300),

                new ShortCost(MARINA, VALD, 100),
                new ShortCost(MARINA, VALD, 200),
                new ShortCost(MARINA, VALD, 300),

                new ShortCost(VALD, MARINA, 100),
                new ShortCost(VALD, MARINA, 500),

                new ShortCost(VALD, VALD, 100),
                new ShortCost(VALD, VALD, 100),
                new ShortCost(VALD, VALD, 100),

                new ShortCost(VALD, MARINA, 100),
                new ShortCost(VALD, MARINA, 100),
        };

        Cost[] result = Calculation.calculate(costs);

        Assert.assertEquals("в итоге должна быть 2 строки", 2, result.length);
        Assert.assertEquals(800, result[0].sum() + result[1].sum(), 0);
    }

    @Test
    public void calculate_5() {

        Cost[] costs = new Cost[1000];
        for (int i = 0; i < 1000; i = i + 2) {
            costs[i] = new ShortCost(EVGENY, MARINA, i);
            costs[i + 1] = new ShortCost(MARINA, EVGENY, i);
        }
        Cost[] result = Calculation.calculate(costs);
        Assert.assertEquals("в итоге должна быть 0 строк", 0, result.length);
    }
}
