package com.pechenkin.travelmoney.cost;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Для агрегации информации по нескольким проводкам.
 * Что бы можно было группировать
 */
public class GroupCost implements Cost {
    private double sum = 0;
    private String comment;
    private List<Cost> costs;
    private Date date;
    private String image_dir;

    public GroupCost(Cost cost) {
        this.costs = new ArrayList<>();
        this.comment = cost.comment();
        this.date = cost.date();
        this.image_dir = cost.image_dir();

        if (cost.active() != 0) {
            this.sum = cost.sum();
        }

        this.costs.add(cost);

    }

    public void addCost(Cost cost) throws Exception {

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
