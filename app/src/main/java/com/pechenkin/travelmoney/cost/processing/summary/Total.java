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

        if (!cost.isRepayment()) {  //операции возврата не считаем тратой, поэтому не добавляем

            if (!members.containsKey(cost.getToMember()))
                members.put(cost.getToMember(), new MemberSum(cost.getToMember()));

            Objects.requireNonNull(members.get(cost.getToMember())).addSumIn(cost.getSum());
        }


        if (!members.containsKey(cost.getMember()))
            members.put(cost.getMember(), new MemberSum(cost.getMember()));

        Objects.requireNonNull(members.get(cost.getMember())).addSumOut(cost.getSum());
    }

    @Override
    public void postIterate() {

        for (int i = 0; i < members.size(); i++) {
            long key = members.keyAt(i);
            MemberSum value = members.get(key);
            if (value != null) {
                result.add(new MemberSum(value.memberId, value.sumIn, value.sumOut));
            }
        }

    }

    public MemberSum[] getResult() {
        return  result.toArray(new MemberSum[0]);
    }

    public static class MemberSum {
        final long memberId;
        double sumIn = 0f;
        double sumOut = 0f;

        private MemberSum(long memberId) {
            this.memberId = memberId;
        }

        MemberSum(long memberId, double sumIn, double sumOut) {
            this.memberId = memberId;
            this.sumIn = sumIn;
            this.sumOut = sumOut;
        }

        private void addSumIn(double sum) {
            this.sumIn += sum;
        }

        private void addSumOut(double sum) {
            this.sumOut += sum;
        }

        public long getMemberId() {
            return memberId;
        }

        public double getSumIn() {
            return sumIn;
        }

        public double getSumOut() {
            return sumOut;
        }
    }
}
