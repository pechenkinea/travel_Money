package com.pechenkin.travelmoney.cost.processing.summary;

import androidx.collection.LongSparseArray;

import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.processing.CostIterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by pechenkin on 13.06.2018.
 * Расчет общей статистики
 */

public class Total implements CostIterable {

    private LongSparseArray<MemberSum> members;
    private List<MemberSum> result = new ArrayList<>();

    public Total() {
        members = new LongSparseArray<>();
    }


    @Override
    public void iterate(Cost cost) {
        if (!cost.isActive()) {
            return;
        }

        if (!members.containsKey(cost.getToMember()))
            members.put(cost.getToMember(), new MemberSum(cost.getToMember()));

        if (!members.containsKey(cost.getMember()))
            members.put(cost.getMember(), new MemberSum(cost.getMember()));


        if (!cost.isRepayment()) {  //операции возврата не считаем тратой, поэтому не добавляем
            Objects.requireNonNull(members.get(cost.getToMember())).addSumExpense(cost.getSum());
        } else {
            //но операция возврата уменьшает "дебет" того, кому отдали долг
            Objects.requireNonNull(members.get(cost.getToMember())).removeSumPay(cost.getSum());
        }

        Objects.requireNonNull(members.get(cost.getMember())).addSumPay(cost.getSum());
    }

    @Override
    public void postIterate() {

        for (int i = 0; i < members.size(); i++) {
            long key = members.keyAt(i);
            MemberSum value = members.get(key);
            if (value != null) {
                result.add(new MemberSum(value.memberId, value.sumExpense, value.sumPay));
            }
        }
    }

    public MemberSum[] getResult() {
        return result.toArray(new MemberSum[0]);
    }

    public static class MemberSum {
        final long memberId;
        double sumExpense = 0f;
        double sumPay = 0f;

        private MemberSum(long memberId) {
            this.memberId = memberId;
        }

        MemberSum(long memberId, double sumExpense, double sumPay) {
            this.memberId = memberId;
            this.sumExpense = sumExpense;
            this.sumPay = sumPay;
        }

        private void addSumExpense(double sum) {
            this.sumExpense += sum;
        }

        private void addSumPay(double sum) {
            this.sumPay += sum;
        }

        private void removeSumPay(double sum) {
            this.sumPay -= sum;
        }

        public long getMemberId() {
            return memberId;
        }

        public double getSumExpense() {
            return sumExpense;
        }

        public double getSumPay() {
            return sumPay;
        }
    }
}
