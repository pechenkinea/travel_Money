package com.pechenkin.travelmoney.cost;

import java.util.Date;

/**
 * Created by pechenkin on 06.04.2018.
 * Для итогов и дополнительных строк листа операций
 */

public class ShortCost implements Cost {

    static private int numerator = 0;

    private int id = numerator++;

    private  boolean isChange = false;

    public  long member;
    private long to_member;
    public  double sum;
    private String comment = "";
    private  int groupId = 0;

    public ShortCost(long member, long to_member, double sum)
    {
        this.member = member;
        this.to_member = to_member;
        this.sum = sum;
    }

    public ShortCost(long member, long to_member, double sum, String comment)
    {
        this.member = member;
        this.to_member = to_member;
        this.sum = sum;
        this.comment = comment;
    }

    public ShortCost(long member, long to_member, double sum, String comment, int groupId)
    {
        this.member = member;
        this.to_member = to_member;
        this.sum = sum;
        this.comment = comment;
        this.groupId = groupId;
    }


    public boolean isChange() {
        return isChange;
    }
    public void setChange(boolean value) {
        isChange = value;
    }

    @Override
    public long id() {
        return -1;
    }

    @Override
    public long member() {
        return member;
    }

    @Override
    public long to_member() {
        return to_member;
    }

    @Override
    public double sum() {
        return sum;
    }

    @Override
    public long active() {
        return -1;
    }

    @Override
    public void setActive(int i) {

    }

    @Override
    public String image_dir() {
        return "";
    }

    @Override
    public Date date() {
        return null;
    }

    @Override
    public String comment() {
        return comment;
    }

    @Override
    public void setGroupId(int groupId){
        this.groupId = groupId;
    }

    @Override
    public int getGroupId() {
        return groupId;
    }


    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof ShortCost && ((ShortCost) obj).id == this.id);
    }
}
