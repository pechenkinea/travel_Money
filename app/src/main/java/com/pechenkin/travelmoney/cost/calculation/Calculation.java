package com.pechenkin.travelmoney.cost.calculation;

import android.util.LongSparseArray;

import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.ShortCost;

import java.util.ArrayList;


/**
 * Класс для проведения расчетов
 */
public class Calculation {

    //Погрешность
    private static final float deviation = 0.01f;

    /**
     * Считает кто кому сколько должен
     *
     * @param list массив всех проводок, которые надо посчитать
     * @return массив проводок где указано не кто кому сколько дал а кто кому сколько должен
     */
    public static ShortCost[] calculate(Cost[] list) {

        LongSparseArray<MemberSum> members = new LongSparseArray<>();

        /*
         * Формируем мапу участников и сразу считаем им сумму
         * Кто дал денег - прибавляем сумму
         * Кому дали денег - отнимаем сумму
         */
        for (Cost cost : list) {

            // если проводка помечена как удаленная или участник дал сам себе то не учитываем такую проводку
            if (cost.isActive() == 0 || cost.getMember() == cost.getToMember()) {
                continue;
            }

            // если в мапе еще нет участника  cost.getMember() добавляем его с нулевой суммой
            if (members.get(cost.getMember()) == null)
                members.put(cost.getMember(), new MemberSum(cost.getMember()));


            members.get(cost.getMember()).addSum(cost.getSum()); // Добавляем сумму

            // если в мапе еще нет участника  cost.getToMember() добавляем его с нулевой суммой
            if (members.get(cost.getToMember()) == null)
                members.put(cost.getToMember(), new MemberSum(cost.getToMember()));

            members.get(cost.getToMember()).removeSum(cost.getSum()); // Отнимаем сумму
        }


        //Формируем списки с положительной суммой (positiveMember) и с отрицательной суммой (negativeMember)
        ArrayList<MemberSum> positiveMember = new ArrayList<>();
        ArrayList<MemberSum> negativeMember = new ArrayList<>();

        for (int i = 0, membersSize = members.size(); i < membersSize; i++) {
            MemberSum value = members.valueAt(i);
            // Если кто то дал или взял меньше погрешности то не учитываем такого участника,
            // что бы в итоговом списке не было совсем копеечных значений
            if (value.sum > deviation) {
                positiveMember.add(value);
            } else if (value.sum < (deviation * -1)) {
                negativeMember.add(value);
            }
        }


        // Формируем итоговый список
        // т.к. при добавлении суммы одному у другого такую же сумму отнимаем сумма положительных будет равна сумме отрицательных
        // остается только распределить эти суммы
        ArrayList<ShortCost> resultList = new ArrayList<>();

        // перебираем участников с положительной суммой т.к. главное получить то что отдал а не отдать то, что получил
        for (MemberSum positive : positiveMember) {

            // сумму positive закрываем суммами должников до тех пор пока не покроем всю сумму
            for (int i = negativeMember.size() - 1; i >= 0; i--) //  перебираем в обратном направлении, что бы была возможность удалять
            {
                MemberSum negative = negativeMember.get(i);

                if (negative.sum * -1 == positive.sum) {
                    // Если долг negative равен сумме positive то добавлем в итог всю сумму и удаляем из списка negativeMember этого должника
                    resultList.add(new ShortCost(negative.id, positive.id, positive.sum));
                    negativeMember.remove(i);
                    // обнуляем сумму positive т.к. есть значение для иога
                    positive.removeSum(positive.sum);
                    break; // сумма positive закрыта. выходим из цикла должников
                } else if (negative.sum * -1 > positive.sum) {
                    // Если долг negative больше суммы positive то закрываем всю сумму positive и сокращаем долг negative на сумму positive
                    resultList.add(new ShortCost(negative.id, positive.id, positive.sum));
                    //добавляем т.к. сумма negative отрицательная
                    negative.addSum(positive.sum);
                    // обнуляем сумму positive т.к. есть значение для иога
                    positive.removeSum(positive.sum);
                    break; // сумма positive закрыта. выходим из цикла должников
                } else {
                    // Если долг negative меньше суммы positive то закрываем сумму positive на весь долг negative и удаляем из списка negativeMember этого должника
                    resultList.add(new ShortCost(negative.id, positive.id, negative.sum * -1));
                    // сокращаем сумму positive т.к. на эту сумму есть значение для иога
                    positive.removeSum(negative.sum * -1);
                    negativeMember.remove(i);
                }
            }

            // Если поле обхода всех должников сумма positive меньше нуля, значит где то был косяк и об этом говорим в приложении дополнительной строкой итога
            if (positive.sum < 0) {
                resultList.add(new ShortCost(-1, -1, 0f, "Ошибка " + t_members.getMemberById(positive.id).name + " (отрицательная сумма)"));
            }

            // Если поле обхода всех должников сумма positive сильно больше погрешности,
            // значит получилось так, что было много участников с долгом меньше погрешности и не получилось до конца закрыть сумму positive
            // говорим об этом в приложении дополнительной строкой итога
            if (positive.sum > deviation * 10) {
                resultList.add(new ShortCost(-1, -1, 0f, "Ошибка " + t_members.getMemberById(positive.id).name + " (Остаток " + positive.sum + ")"));
            }

        }


        return resultList.toArray(new ShortCost[0]);

    }


    /**
     * Вместо Id участников ставит id их цветов и закидывает на пересчет
     *
     * @param calculationList лист с итогами (<b>кто кому должен</b>)
     * @return лист с итогами с учетом того, что у одинаковых цветов один бюджет
     */
    public static ShortCost[] groupByColor(Cost[] calculationList) {

        if (calculationList.length == 0)
            return new ShortCost[0];

        Cost[] calcListCosts = new Cost[calculationList.length];
        LongSparseArray<Long> membersByColor = new LongSparseArray<>();
        for (int i = 0; i < calculationList.length; i++) {

            Cost calcCost = calculationList[i];
            int memberColor = t_members.getColorById(calcCost.getMember());
            int to_memberColor = t_members.getColorById(calcCost.getToMember());

            membersByColor.put(to_memberColor, calcCost.getToMember());
            if (membersByColor.indexOfKey(memberColor) < 0) {
                membersByColor.put(memberColor, calcCost.getMember());
            }

            // т.к. в calculationList приходит "кто кому должен" надо перевернуть значения, что бы получилось "кто кому дал"
            // поэтому первым параметром в ShortCost отдаем getToMember а вторым getMember
            Cost forGroupCost = new ShortCost(to_memberColor, memberColor, calcCost.getSum());
            calcListCosts[i] = forGroupCost;
        }


        calculationList = Calculation.calculate(calcListCosts);


        ShortCost[] result = new ShortCost[calculationList.length];
        //Переводим цвета обратно в учатников что бы вывести в список
        for (int i = 0; i < calculationList.length; i++) {

            ShortCost c = new ShortCost(
                    membersByColor.get(calculationList[i].getMember()),
                    membersByColor.get(calculationList[i].getToMember()),
                    calculationList[i].getSum()
            );

            result[i] = c;

        }


        return result;


    }

    /**
     * Хранит итогувую сумму для участника
     */
    private static class MemberSum {
        final long id;
        double sum = 0f;

        /**
         * Создает новый объект MemberSum с нулевой суммой
         *
         * @param id Id участника
         */
        MemberSum(long id) {
            this.id = id;
        }

        /**
         * Добавляет сумму к текущему значению.
         * Для случаев, когда участник кому то дал денег
         *
         * @param sum сумма проводки
         */
        void addSum(double sum) {
            this.sum += sum;
        }

        /**
         * Вычитает от текущего значения переданную сумму
         * Для случаев, когда участник получил от кого то денег
         *
         * @param sum сумма проводки
         */
        void removeSum(double sum) {
            this.sum -= sum;
        }
    }


}