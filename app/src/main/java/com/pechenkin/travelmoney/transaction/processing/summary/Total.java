package com.pechenkin.travelmoney.transaction.processing.summary;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.transaction.processing.CostIterable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by pechenkin on 13.06.2018.
 * Расчет общей статистики
 */

public class Total implements CostIterable {

    private final Map<String, MemberSum> members;
    private final List<MemberSum> result = new ArrayList<>();

    public Total() {
        members = new HashMap<>();
    }


    @Override
    public void iterate(Transaction transaction) {
        if (!transaction.isActive()) {
            return;
        }

        for (TransactionItem item : transaction.getDebitItems()) {

            if (!members.containsKey(item.getMemberUuid()))
                members.put(item.getMemberUuid(), new MemberSum(item.getMember()));


            if (!transaction.isRepayment()) {  //операции возврата не считаем тратой, поэтому не добавляем
                Objects.requireNonNull(members.get(item.getMemberUuid())).addSumExpense(item.getDebit());
            } else {
                //но операция возврата уменьшает "дебет" того, кому отдали долг
                Objects.requireNonNull(members.get(item.getMemberUuid())).removeSumPay(item.getDebit());
            }
        }


        for (TransactionItem item : transaction.getCreditItems()) {

            if (!members.containsKey(item.getMemberUuid()))
                members.put(item.getMemberUuid(), new MemberSum(item.getMember()));

            Objects.requireNonNull(members.get(item.getMemberUuid())).addSumPay(item.getCredit());
        }


    }

    @Override
    public void postIterate() {

        for (MemberSum value : members.values()) {
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
        int sumExpense = 0;
        int sumPay = 0;

        private MemberSum(Member member) {
            this.member = member;
        }

        MemberSum(Member member, int sumExpense, int sumPay) {
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

        public int getSumExpense() {
            return sumExpense;
        }

        public int getSumPay() {
            return sumPay;
        }
    }
}
