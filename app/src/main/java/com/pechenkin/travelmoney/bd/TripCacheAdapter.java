package com.pechenkin.travelmoney.bd;


import java.util.List;

/**
 * Кеширование значений методов. Нужно, что бы лишний раз не лазить в базу (для удаленной БД особенно актуально).
 */
public class TripCacheAdapter extends TripAdapter {
    public TripCacheAdapter(Trip trip) {
        super(trip);
    }

    private List<Member> allMembers = null;
    private List<Member> activeMembers = null;

    @Override
    public List<Member> getAllMembers() {
        if (allMembers == null) {
            allMembers = super.getAllMembers();
        }
        return allMembers;
    }

    @Override
    public List<Member> getActiveMembers() {
        if (activeMembers == null) {
            activeMembers = super.getActiveMembers();
        }
        return activeMembers;
    }

    @Override
    public Member createMember(String name, int color, int icon) {
        Member createdMember = super.createMember(name, color, icon);
        //allMembers.add(createdMember);
        return createdMember;
    }
}
