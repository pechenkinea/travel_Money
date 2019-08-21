package com.pechenkin.travelmoney.bd;

public class TripCacheAdapter extends TripAdapter {
    public TripCacheAdapter(Trip trip) {
        super(trip);
    }

    private Member[] allMembers = null;
    private Member[] activeMembers = null;

    @Override
    public Member[] getAllMembers() {
        if (allMembers == null) {
            allMembers = super.getAllMembers();
        }
        return allMembers;
    }

    @Override
    public Member[]getActiveMembers() {
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
