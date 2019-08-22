package com.pechenkin.travelmoney.bd;

import com.pechenkin.travelmoney.cost.Cost;

import java.util.Date;
import java.util.List;

/**
 * Адаптер для поездки. ничего не далет. просто вызвает методы из вложенного Trip
 * Нужно для того, что бы в классе для кэширования не переопределять все методы. а только те, которые работают с кэшем
 */
public class TripAdapter implements Trip {

    protected Trip trip;

    TripAdapter(Trip trip) {
        this.trip = trip;
    }

    @Override
    public long getId() {
        return this.trip.getId();
    }

    @Override
    public String getName() {
        return this.trip.getName();
    }

    @Override
    public String getComment() {
        return this.trip.getComment();
    }

    @Override
    public void edit(String name, String comment) {
        this.trip.edit(name, comment);
    }

    @Override
    public boolean isActive() {
        return this.trip.isActive();
    }

    @Override
    public Date getStartDate() {
        return this.trip.getStartDate();
    }

    @Override
    public Date getEndDate() {
        return this.trip.getEndDate();
    }

    @Override
    public List<Member> getAllMembers() {
        return this.trip.getAllMembers();
    }

    @Override
    public List<Member> getActiveMembers() {
        return this.trip.getActiveMembers();
    }

    @Override
    public Member createMember(String name, int color, int icon) {
        return this.trip.createMember(name, color, icon);
    }

    @Override
    public Member getMe() {
        return this.trip.getMe();
    }

    @Override
    public Member getMemberById(long id) {
        return this.trip.getMemberById(id);
    }

    @Override
    public Member getMemberByName(String name) {
        return this.trip.getMemberByName(name);
    }

    @Override
    public boolean memberIsActive(Member member) {
        return this.trip.memberIsActive(member);
    }

    @Override
    public void setMemberActive(Member member, boolean active) {
        this.trip.setMemberActive(member, active);
    }

    @Override
    public List<Cost> getAllCost() {
        return this.trip.getAllCost();
    }

    @Override
    public void addCost(Member member, Member toMember, String comment, double sum, String image_dir, Date date, boolean isRepayment) {
        this.trip.addCost(member, toMember, comment, sum, image_dir, date, isRepayment);
    }


}
