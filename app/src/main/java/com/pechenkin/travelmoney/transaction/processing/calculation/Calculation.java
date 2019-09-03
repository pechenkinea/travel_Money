package com.pechenkin.travelmoney.transaction.processing.calculation;

import android.util.LongSparseArray;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.transaction.adapter.CostListItem;
import com.pechenkin.travelmoney.transaction.list.LabelItem;
import com.pechenkin.travelmoney.transaction.list.TotalItem;
import com.pechenkin.travelmoney.transaction.processing.CostIterable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Класс для проведения расчетов
 * Считает кто кому сколько должен
 */
public class Calculation implements CostIterable {

    //Погрешность
    private static final float deviation = 0.01f;

    private LongSparseArray<MemberSum> members;
    private List<CostListItem> result;

    //Нужно для того, что бы можно было считать не по id сотрудников а, например, по их цветам
    private MemberUidGetterID memberUidGetter;


    public Calculation(boolean needGroupByColor) {

        if (needGroupByColor) {
            this.memberUidGetter = new MemberUidGetterColor();
        } else {
            this.memberUidGetter = new MemberUidGetterID();
        }

        this.members = new LongSparseArray<>();
    }

    /*
     * Формируем мапу участников и сразу считаем им сумму
     * Кто дал денег - прибавляем сумму
     * Кому дали денег - отнимаем сумму
     */
    @Override
    public void iterate(Transaction transaction) {

        // если проводка помечена как удаленная то не учитываем такую проводку
        if (!transaction.isActive()) {
            return;
        }

        for (TransactionItem item : transaction.getCreditItems()) {
            // если в мапе еще нет участника  item.getMember() добавляем его с нулевой суммой
            if (members.get(memberUidGetter.getMemberUid(item.getMember())) == null)
                members.put(memberUidGetter.getMemberUid(item.getMember()), new MemberSum(item.getMember()));

            members.get(memberUidGetter.getMemberUid(item.getMember())).addSum(item.getCredit()); // Добавляем сумму
        }

        for (TransactionItem item : transaction.getDebitItems()) {
            if (members.get(memberUidGetter.getMemberUid(item.getMember())) == null)
                members.put(memberUidGetter.getMemberUid(item.getMember()), new MemberSum(item.getMember()));

            members.get(memberUidGetter.getMemberUid(item.getMember())).removeSum(item.getDebit()); // Отнимаем сумму
        }

    }

    @Override
    public void postIterate() {

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


        Collections.sort(positiveMember, new MemberSumComparatorDesc());
        Collections.sort(negativeMember, new MemberSumComparatorDesc());

        // Формируем итоговый список
        // т.к. при добавлении суммы одному у другого такую же сумму отнимаем сумма положительных будет равна сумме отрицательных
        // остается только распределить эти суммы
        ArrayList<CostListItem> resultList = new ArrayList<>();

        // перебираем участников с положительной суммой т.к. главное получить то что отдал а не отдать то, что получил
        for (MemberSum positive : positiveMember) {

            boolean isClose = false;

            //Смотрим, есть ли среди должников тот, кто должен ровно столько сколько надо positive
            for (int i = negativeMember.size() - 1; i >= 0; i--) {
                MemberSum negative = negativeMember.get(i);
                if (negative.sum * -1 == positive.sum) {
                    resultList.add(new TotalItem(negative.member, positive.member, positive.sum));
                    negativeMember.remove(i);
                    positive.removeSum(positive.sum);
                    isClose = true;
                    break;
                }
            }

            if (isClose) {
                continue;
            }


            // сумму positive закрываем суммами должников до тех пор пока не покроем всю сумму
            for (int i = negativeMember.size() - 1; i >= 0; i--) //  перебираем в обратном направлении, что бы была возможность удалять
            {
                MemberSum negative = negativeMember.get(i);

                if (negative.sum * -1 > positive.sum) {
                    // Если долг negative больше суммы positive то закрываем всю сумму positive и сокращаем долг negative на сумму positive
                    resultList.add(new TotalItem(negative.member, positive.member, positive.sum));
                    //добавляем т.к. сумма negative отрицательная
                    negative.addSum(positive.sum);
                    // обнуляем сумму positive т.к. есть значение для иога
                    positive.removeSum(positive.sum);
                    break; // сумма positive закрыта. выходим из цикла должников
                } else {
                    // Если долг negative меньше суммы positive то закрываем сумму positive на весь долг negative и удаляем из списка negativeMember этого должника
                    resultList.add(new TotalItem(negative.member, positive.member, negative.sum * -1));
                    // сокращаем сумму positive т.к. на эту сумму есть значение для иога
                    positive.removeSum(negative.sum * -1);
                    negativeMember.remove(i);
                }
            }

            // Если поле обхода всех должников сумма positive меньше нуля, значит где то был косяк и об этом говорим в приложении дополнительной строкой итога
            if (positive.sum < 0) {
                //TODO сделать отдельный класс для отображения ошибок LabelItem не очень подходит
                resultList.add(new LabelItem(positive.member.getName() + " (отрицательная сумма)"));
            }

            // Если поле обхода всех должников сумма positive сильно больше погрешности,
            // значит получилось так, что было много участников с долгом меньше погрешности и не получилось до конца закрыть сумму positive
            // (например если более 100 участников должны кому то по 0.01)
            // говорим об этом в приложении дополнительной строкой итога
            if (positive.sum > deviation * 100) {
                //TODO сделать отдельный класс для отображения ошибок LabelItem не очень подходит
                resultList.add(new LabelItem(positive.member.getName() + " (Остаток " + positive.sum + ")"));
            }
        }


        result = resultList;

    }


    public List<CostListItem> getResult() {
        return result;
    }

    /**
     * Хранит итогувую сумму для участника
     */
    private static class MemberSum {
        final Member member;
        int sum = 0;

        /**
         * Создает новый объект MemberSum с нулевой суммой
         */
        MemberSum(Member member) {
            this.member = member;
        }

        /**
         * Добавляет сумму к текущему значению.
         * Для случаев, когда участник кому то дал денег
         *
         * @param sum сумма проводки
         */
        void addSum(int sum) {
            this.sum += sum;
        }

        /**
         * Вычитает от текущего значения переданную сумму
         * Для случаев, когда участник получил от кого то денег
         *
         * @param sum сумма проводки
         */
        void removeSum(int sum) {
            this.sum -= sum;
        }
    }

    private class MemberSumComparatorDesc implements Comparator<MemberSum> {
        public int compare(MemberSum left, MemberSum right) {
            return Double.compare(right.sum, left.sum);
        }
    }


    private class MemberUidGetterID {
        long getMemberUid(Member member) {
            return member.getId();
        }
    }

    private class MemberUidGetterColor extends MemberUidGetterID {
        @Override
        long getMemberUid(Member member) {
            return member.getColor();
        }
    }


}
