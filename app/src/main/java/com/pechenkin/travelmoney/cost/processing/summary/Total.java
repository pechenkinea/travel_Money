package com.pechenkin.travelmoney.cost.processing.summary;

import androidx.collection.LongSparseArray;

import com.pechenkin.travelmoney.bd.Member;
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

        if (!members.containsKey(cost.getToMember().getId()))
            members.put(cost.getToMember().getId(), new MemberSum(cost.getToMember()));

        if (!members.containsKey(cost.getMember().getId()))
            members.put(cost.getMember().getId(), new MemberSum(cost.getMember()));


        if (!cost.isRepayment()) {  //операции возврата не считаем тратой, поэтому не добавляем
            Objects.requireNonNull(members.get(cost.getToMember().getId())).addSumExpense(cost.getSum());
        } else {
            //но операция возврата уменьшает "дебет" того, кому отдали долг
            Objects.requireNonNull(members.get(cost.getToMember().getId())).removeSumPay(cost.getSum());
        }

        Objects.requireNonNull(members.get(cost.getMember().getId())).addSumPay(cost.getSum());
    }

    @Override
    public void postIterate() {

        for (int i = 0; i < members.size(); i++) {
            long key = members.keyAt(i);
            MemberSum value = members.get(key);
            if (value != null) {
                result.add(new MemberSum(value.member, value.sumExpense, value.sumPay));
            }
        }
    }

    public List<MemberSum> getResult() {
        return result;
    }

    public static class MemberSum {
        final Member member;
        double sumExpense = 0f;
        double sumPay = 0f;

        private MemberSum(Member member) {
            this.member = member;
        }

        MemberSum(Member member, double sumExpense, double sumPay) {
            this.member = member;
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

        public Member getMember() {
            return member;
        }

        public double getSumExpense() {
            return sumExpense;
        }

        public double getSumPay() {
            return sumPay;
        }
    }
}
