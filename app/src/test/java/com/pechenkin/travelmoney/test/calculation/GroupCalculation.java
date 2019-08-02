package com.pechenkin.travelmoney.test.calculation;

import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.calculation.Calculation;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.ShortCost;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
public class GroupCalculation {

    @PrepareForTest({t_members.class})
    @Test
    public void calculate_1() {
        Cost[] costs = new Cost[]{
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 100),
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 100),
                new ShortCost(TestMembers.EVGENIY, TestMembers.MARINA, 100),

                new ShortCost(TestMembers.MARINA, TestMembers.EVGENIY, 200),

                new ShortCost(TestMembers.VLAD, TestMembers.MARINA, 200),
                new ShortCost(TestMembers.VLAD, TestMembers.EVGENIY, 200),


        };

        //Заглушка. EVGENIY и MARINA одного цвета а VLAD другого
        PowerMockito.mockStatic(t_members.class);
        PowerMockito.when(t_members.getColorById(TestMembers.EVGENIY)).thenReturn(1);
        PowerMockito.when(t_members.getColorById(TestMembers.MARINA)).thenReturn(1);
        PowerMockito.when(t_members.getColorById(TestMembers.VLAD)).thenReturn(2);


        Cost[] result = Calculation.calculate(costs);
        Assert.assertEquals("в итоге должна быть 2 строки", 2, result.length);

        Cost[] groupResult = Calculation.groupByColor(result);
        Assert.assertEquals("в итоге должна быть 1 строка", 1, groupResult.length);

        Assert.assertEquals(TestMembers.VLAD, groupResult[0].to_member());
        Assert.assertEquals(400, groupResult[0].sum(), 0);

    }

    @PrepareForTest({t_members.class})
    @Test
    public void calculate_2() {
        Cost[] costs = new Cost[]{
                new ShortCost(TestMembers.EVGENIY, TestMembers.GREEN, 100),
                new ShortCost(TestMembers.EVGENIY, TestMembers.GREEN, 100),

                new ShortCost(TestMembers.SVETA, TestMembers.MARINA, 50),
                new ShortCost(TestMembers.SVETA, TestMembers.MARINA, 50),
                new ShortCost(TestMembers.SVETA, TestMembers.MARINA, 50),
                new ShortCost(TestMembers.SVETA, TestMembers.MARINA, 50),
        };

        PowerMockito.mockStatic(t_members.class);
        PowerMockito.when(t_members.getColorById(TestMembers.EVGENIY)).thenReturn(1);
        PowerMockito.when(t_members.getColorById(TestMembers.MARINA)).thenReturn(1);
        PowerMockito.when(t_members.getColorById(TestMembers.GREEN)).thenReturn(2);
        PowerMockito.when(t_members.getColorById(TestMembers.SVETA)).thenReturn(2);


        Cost[] result = Calculation.calculate(costs);
        Assert.assertEquals("в итоге должна быть 2 строки", 2, result.length);

        Cost[] groupResult = Calculation.groupByColor(result);
        Assert.assertEquals("в итоге должна быть 0 строк", 0, groupResult.length);
    }

}
