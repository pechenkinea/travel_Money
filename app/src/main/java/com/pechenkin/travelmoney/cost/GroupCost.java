package com.pechenkin.travelmoney.cost;

import com.pechenkin.travelmoney.Help;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Для агрегации информации по нескольким проводкам.
 * Что бы можно было группировать
 */
public class GroupCost implements Cost {

    /**
     * группирует операции по дате и комментарию
     *
     * @param costs отсортированный по дате массив трат
     */
    public static GroupCost[] group(Cost[] costs) {
        List<GroupCost> groupCostList = new ArrayList<>();
        String lastKey = "";

        for (Cost cost : costs) {
            String key = cost.date().getTime() + cost.comment();
            if (key.equals(lastKey)) {
                try {
                    groupCostList.get(groupCostList.size() - 1).addCost(cost);
                } catch (Exception ex) {
                    Help.alert(ex.getMessage());
                    return null;
                }
            } else {
                groupCostList.add(new GroupCost(cost));
                lastKey = key;
            }
        }

        return groupCostList.toArray(new GroupCost[0]);
    }

    private double sum = 0;
    private final String comment;
    private final List<Cost> costs;
    private final Date date;
    private final String image_dir;

    private GroupCost(Cost cost) {
        this.costs = new ArrayList<>();
        this.comment = cost.comment();
        this.date = cost.date();
        this.image_dir = cost.image_dir();

        if (cost.active() != 0) {
            this.sum = cost.sum();
        }

        this.costs.add(cost);

    }

    private void addCost(Cost cost) throws Exception {

        if (this.date.getTime() != cost.date().getTime() || !this.comment.equals(cost.comment())) {
            throw new Exception("В группу пробует добавится проводка, которая к ней не относится");
        }

        if (cost.active() != 0) {
            this.sum += cost.sum();
        }
        this.costs.add(cost);
    }

    public void updateSum() {
        this.sum = 0;
        for (Cost cost : costs) {
            if (cost.active() != 0) {
                this.sum += cost.sum();
            }
        }
    }


    public List<Cost> getCosts() {
        return costs;
    }


    @Override
    public long id() {
        return -1;
    }

    @Override
    public long member() {
        return -1;
    }

    @Override
    public long to_member() {
        return -1;
    }

    @Override
    public double sum() {
        return sum;
    }

    @Override
    public long active() {
        return 1;
    }

    @Override
    public void setActive(int i) {

    }

    @Override
    public String image_dir() {
        return image_dir;
    }

    @Override
    public Date date() {
        return date;
    }

    @Override
    public String comment() {
        return comment;
    }

    @Override
    public void setGroupId(int groupId) {

    }

    @Override
    public int getGroupId() {
        return 0;
    }
}
